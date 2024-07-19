package umc6.tom.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.PhoneHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.security.JwtToken;
import umc6.tom.security.KeyUtil;
import umc6.tom.security.RedisUtil;
import umc6.tom.security.config.JwtTokenProvider;
import umc6.tom.user.converter.UserConverter;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.UserStatus;
import umc6.tom.user.repository.UserRepository;

import java.security.Key;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;


    @Override
    public User join(UserDtoReq.JoinDto request) {

        if (duplicatedNickName(request.getNickName())) {
            throw new UserHandler(ErrorStatus.USER_NICKNAME_DUPLICATED);
        }   // 닉네임 중복 확인
        if (duplicatedAccount(request.getAccount())) {
            throw new UserHandler(ErrorStatus.USER_ACCOUNT_DUPLICATED);
        }   // 아이디 중복 확인 - 에러 핸들러 작성

        User user = UserConverter.toUser(request);

        userRepository.save(user);
        return user;
    }

    // user 이름으로 user 찾기
    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    // 로그인
    @Override
    public UserDtoRes.signInDto signIn(UserDtoReq.SignInDto request) {

        String account = request.getAccount();
        String password = request.getPassword();

        User user = userRepository.findByAccountAndStatus(account, UserStatus.active)
                .or(() -> userRepository.findByAccountAndStatus(account, UserStatus.inactive))
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (user.getStatus() == UserStatus.inactive) {
            throw new PhoneHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        // userId + password 기반 토큰 생성
        // 유저 id + password 를 기반으로 Authentication 객체 생성
        Long userId = user.getId();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);

        // 인증 정보 검증 - authenticate() 메서드를 통해 등록된 User 에 대한 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        String accessToken = jwtToken.getAccessToken();
        String refreshToken = jwtToken.getRefreshToken();

        // 키 생성
        Key redisKey = KeyUtil.generateKey("RT:" + user.getId());

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

    @Override
    public boolean duplicatedPhone(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }
}
