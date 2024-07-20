package umc6.tom.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.MajorHandler;
import umc6.tom.apiPayload.exception.handler.PhoneHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.common.model.Majors;
import umc6.tom.security.JwtToken;
import umc6.tom.security.KeyUtil;
import umc6.tom.security.RedisUtil;
import umc6.tom.security.config.JwtTokenProvider;
import umc6.tom.user.converter.ResignConverter;
import umc6.tom.user.converter.UserConverter;
import umc6.tom.user.dto.ResignDtoReq;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.Resign;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.UserStatus;
import umc6.tom.user.repository.MajorsRepository;
import umc6.tom.user.repository.ResignRepository;
import umc6.tom.user.repository.UserRepository;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final ResignRepository resignRepository;
    private final MajorsRepository majorsRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원 가입
    @Override
    public User join(UserDtoReq.JoinDto request) {

        if (duplicatedNickName(request.getNickName())) {
            throw new UserHandler(ErrorStatus.USER_NICKNAME_DUPLICATED);
        }   // 닉네임 중복 확인
        if (duplicatedAccount(request.getAccount())) {
            throw new UserHandler(ErrorStatus.USER_ACCOUNT_DUPLICATED);
        }   // 아이디 중복 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NOT_EQUAL);
        }

        Majors major = majorsRepository.findById(request.getMajor())
                .orElseThrow(() -> new MajorHandler(ErrorStatus.MAJORS_NOR_FOUND));

        // 비밀번호 암호화 후 저장
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = UserConverter.toUser(request, major);

        userRepository.save(user);
        return user;
    }

    // 로그인
    @Override
    public UserDtoRes.signInDto signIn(UserDtoReq.SignInDto request) {

        String account = request.getAccount();
        String password = request.getPassword();

        User user = userRepository.findByAccountAndStatus(account, UserStatus.ACTIVE)
                .or(() -> userRepository.findByAccountAndStatus(account, UserStatus.INACTIVE))
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 비밀번호 불일치
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NOT_EQUAL);
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new PhoneHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        // 탈퇴된 유저는 기간내 로그인 할 시 WITHDRAW to ACTIVE, 로그인
        if (user.getStatus() == UserStatus.WITHDRAW) {
            user.setStatus(UserStatus.ACTIVE);
        }

        // userId + password 를 기반으로 Authentication 객체 생성
        Long userId = user.getId();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);

        // 인증 정보 검증 - authenticate() 메서드를 통해 등록된 User 에 대한 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        String accessToken = jwtToken.getAccessToken();
        String refreshToken = jwtToken.getRefreshToken();

        // 키 생성
        Key redisKey = KeyUtil.generateKey("RT:" + user.getId());

        // RefreshToken 을 redis 에 저장
        redisUtil.setDataExpire(redisKey, refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME_IN_REDIS);

        return UserConverter.signInRes(user, accessToken, refreshToken);
    }

    // 닉네임 중복 확인 - 중복이 있다면 true
    @Override
    public boolean duplicatedNickName(String nickName) {
        return userRepository.findByNickName(nickName).isPresent();
    }

    // 아이디 중복 확인 - 중복이 있다면 true
    @Override
    public boolean duplicatedAccount(String account) {
        return userRepository.findByAccount(account).isPresent();
    }

    // 회원 탈퇴 UserStatus ACTIVE -> WITHDRAW
    @Override
    public void withDraw(Long userId, UserDtoReq.WithDrawDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 기존 비밀번호와 일치하는지 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NOT_EQUAL);
        }

        // 회원 탈퇴시 탈퇴 정보 저장
        // 재가입 대기 시간
        final long USER_WAIT_WITHDRAWAL_TIME = 1000L * 60 * 60 * 24;

        ResignDtoReq.saveDto resign = ResignDtoReq.saveDto
                .builder()
                .user(user)
                .reason(request.getReason())
                .timer(convertToLocalDateTime(USER_WAIT_WITHDRAWAL_TIME))
                .build();

        resignRepository.save(ResignConverter.toResign(resign));

        user.setStatus(UserStatus.WITHDRAW);
    }

    // 재가입대기시간 이후 Resign, User 를 삭제하는 메서드
    @Override
    public void deleteUser() {

        List<Resign> resigns = resignRepository.findByTimerBefore(LocalDateTime.now());
        List<User> users = new ArrayList<>();

        if (!resigns.isEmpty()) {
            // 삭제할 User 객체를 빼낸 후 Resign 삭제, 이후 userService 로 전달
            for (Resign resign : resigns) {
                users.add(resign.getUser());
            }
            resignRepository.deleteAll(resigns);
            log.info("Withdraw User's Resign deleted.");
            userRepository.deleteAll(users);
            log.info("Withdraw User deleted.");
        }
    }

    @Override
    public LocalDateTime convertToLocalDateTime(long timestampMillis) {

        long now = new Date().getTime();
        Instant instant = Instant.ofEpochMilli(now + timestampMillis);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());  // Instant 와 ZoneId를 사용하여 LocalDateTime 객체 생성
    }
}
