package umc6.tom.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc6.tom.alarm.converter.AlarmSetConverter;
import umc6.tom.alarm.repository.AlarmSetRepository;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.MajorHandler;
import umc6.tom.apiPayload.exception.handler.PhoneHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.common.model.Majors;
import umc6.tom.security.JwtTokenProvider;
import umc6.tom.user.converter.RefreshConverter;
import umc6.tom.user.converter.ResignConverter;
import umc6.tom.user.converter.UserConverter;
import umc6.tom.user.dto.ResignDtoReq;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.RefreshToken;
import umc6.tom.user.model.Resign;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Agreement;
import umc6.tom.user.model.enums.UserStatus;
import umc6.tom.user.repository.MajorsRepository;
import umc6.tom.user.repository.RefreshTokenRepository;
import umc6.tom.user.repository.ResignRepository;
import umc6.tom.user.repository.UserRepository;
import umc6.tom.util.CookieUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResignRepository resignRepository;
    private final MajorsRepository majorsRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AlarmSetRepository alarmSetRepository;


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
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCHED);
        }
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            log.info("이미 사용중인 휴대폰 번호 등록 ");
            throw new UserHandler(ErrorStatus.USER_PHONE_IS_USED);
        }

        Majors major = majorsRepository.findById(request.getMajor())
                .orElseThrow(() -> new MajorHandler(ErrorStatus.MAJORS_NOR_FOUND));

        // 비밀번호 암호화 후 저장
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = UserConverter.toUser(request, major);

        userRepository.save(user);
        alarmSetRepository.save(AlarmSetConverter.convertAlarmSetToAlarmSet(user));

        return user;
    }

    // 닉네임 중복 확인
    @Override
    public boolean checkNickName(UserDtoReq.CheckNickNameDto request) {

        return duplicatedNickName(request.getNickName());
    }

    // 아이디 중복 확인
    @Override
    public boolean checkAccount(UserDtoReq.CheckAccountDto request) {

        return duplicatedAccount(request.getAccount());
    }

    // 로그인
    @Override
    public UserDtoRes.LoginDto login(HttpServletRequest request, HttpServletResponse response, UserDtoReq.LoginDto req) {

        String account = req.getAccount();
        String password = req.getPassword();

        List<UserStatus> statuses = List.of(UserStatus.ACTIVE, UserStatus.INACTIVE, UserStatus.WITHDRAW);

        User user = userRepository.findByAccountAndStatusIn(account, statuses)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        log.info("로그인 유저 : {} {}", user.getUsername(), user.getStatus());

        // 비밀번호 불일치
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NOT_EQUAL);
        }
        log.info("비밀번호 검증 통과 user : {}", user.getUsername());

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new PhoneHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        // 탈퇴된 유저는 기간내 로그인 할 시 WITHDRAW to ACTIVE, 로그인
        if (user.getStatus() == UserStatus.WITHDRAW) {
            resignRepository.deleteByUser(user);
            user.setStatus(UserStatus.ACTIVE);
            log.info("탈퇴한 유저 {} 재가입", user.getId());
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        log.info("login refresh token : {}", refreshToken);

        // 쿠키 저장
        CookieUtil.deleteCookie(request, response, "refreshToken");
        CookieUtil.addCookie(response, "refreshToken", refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME_IN_COOKIE);
        // DB 의 refreshToken 삭제 후 저장 변경

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUserId(user.getId());
        if (refreshTokenOptional.isPresent()) {
            refreshTokenOptional.get().setRefreshTokenValue(refreshToken);
        } else {
            refreshTokenRepository.save(RefreshConverter.toRefreshToken(user.getId(), refreshToken));
        }

        return UserConverter.signInRes(user, accessToken);
    }

    // 쿠키의 RefreshToken 으로 AccessToken 재발행
    @Override
    public UserDtoRes.ReissueDto reissue(String refreshToken) {

        // 토큰 유효성 검사
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        Long userId = findTokenByRefreshToken(refreshToken).getUserId();
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        return UserConverter.reissueRes(accessToken);
    }

    // String 의 RefreshToken 으로 RefreshToken 객체 반환
    @Override
    public RefreshToken findTokenByRefreshToken(String refreshToken) {

        return refreshTokenRepository.findByRefreshTokenValue(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 RefreshToken 이 없습니다. 다시 로그인해주세요."));  // 예외 수정해야함
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

        // 기존 UserStatus.WITHDRAW or resign 존재시
        if (user.getStatus() == UserStatus.WITHDRAW || resignRepository.findByUser(user).isPresent()) {
            throw new UserHandler(ErrorStatus.USER_ALREADY_WITHDRAW);
        }

        // 회원 탈퇴시 탈퇴 정보 저장, 재가입 대기 시간 24시간(밀리초)
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
    public void logout(HttpServletRequest request, HttpServletResponse response, String accessToken) {

        // Cookie 에 있는 RefreshToken 의 데이터를 value 0, 만료 0 으로 초기화
        CookieUtil.addCookie(response, "refreshToken", null, 0);
    }

    // 밀리초를 LocalDateTime 으로 변환하는 메서드
    @Override
    public LocalDateTime convertToLocalDateTime(long timestampMillis) {

        long now = new Date().getTime();
        Instant instant = Instant.ofEpochMilli(now + timestampMillis);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());  // Instant 와 ZoneId를 사용하여 LocalDateTime 객체 생성
    }

    // 아이디 찾기
    @Override
    public UserDtoRes.FindAccountDto findAccount(UserDtoReq.FindAccountDto request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return UserConverter.findAccountRes(user);
    }

    // 비밀번호 찾기
    @Override
    public UserDtoRes.FindPasswordDto findPassword(UserDtoReq.FindPasswordDto request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (!user.getAccount().equals(request.getAccount())) {
            throw new UserHandler(ErrorStatus.USER_ACCOUNT_NOT_MATCHED);
        }
        if (!user.getName().equals(request.getName())) {
            throw new UserHandler(ErrorStatus.USER_NAME_NOT_MATCHED);
        }

        return UserConverter.findPasswordRes(user);
    }

    // 비밀번호 찾기 후 재설정
    @Override
    public void findRestorePassword(UserDtoReq.FindRestorePasswordDto request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCHED);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    // 아이디 재설정
    @Override
    public void restoreAccount(Long userId, UserDtoReq.RestoreAccountDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (user.getAccount().equals(request.getAccount())) {   // 본인이 사용중인 아이디 확인
            throw new UserHandler(ErrorStatus.USER_ACCOUNT_IS_YOURS);
        } else if (duplicatedAccount(request.getAccount())) {   // 아이디 중복 확인
            throw new UserHandler(ErrorStatus.USER_ACCOUNT_DUPLICATED);
        }
        user.setAccount(request.getAccount());
    }

    // 비밀번호 재설정
    @Override
    public void restorePassword(Long userId, UserDtoReq.RestorePasswordDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getUsedPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NOT_EQUAL);
        }

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCHED);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    // 닉네임 재설정
    @Override
    public void restoreNickName(Long userId, UserDtoReq.RestoreNickNameDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (user.getNickName().equals(request.getNickName())) {
            throw new UserHandler(ErrorStatus.USER_NICKNAME_IS_YOURS);
        }

        if (duplicatedNickName(request.getNickName())) {
            throw new UserHandler(ErrorStatus.USER_NICKNAME_DUPLICATED);
        }
        user.setNickName(request.getNickName());
    }

    // 휴대폰 번호 재설정
    @Override
    public void restorePhone(Long userId, UserDtoReq.RestorePhoneDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (user.getPhone().equals(request.getPhone())) {
            throw new UserHandler(ErrorStatus.USER_PHONE_IS_YOURS);
        }
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            log.info("이미 사용중인 휴대폰 번호 등록");
            throw new UserHandler(ErrorStatus.USER_PHONE_IS_USED);
        }
    }

    // 전공 재설정
    @Override
    public void restoreMajor(Long userId, UserDtoReq.RestoreMajorDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (user.getMajors().getId().equals(request.getMajorId())) {
            throw new MajorHandler(ErrorStatus.USER_MAJOR_IS_YOURS);
        }

        Majors majors = majorsRepository.findById(request.getMajorId())
                        .orElseThrow(() -> new MajorHandler(ErrorStatus.MAJORS_NOR_FOUND));

        user.setMajors(majors);
    }

    // 활동내역 공개 여부 재설정
    @Override
    public UserDtoRes.ChangeAgreementDto changeAgreement(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 다른 값으로 저장되어 있다면 AGREE 로 설정
        if (user.getAgreement() == Agreement.AGREE) {
            user.setAgreement(Agreement.DISAGREE);
        } else {
            user.setAgreement(Agreement.AGREE);
        }
        return UserConverter.changeAgreementRes(user.getAgreement());
    }
}
