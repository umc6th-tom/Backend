package umc6.tom.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.security.JwtToken;
import umc6.tom.security.SecurityUtil;
import umc6.tom.user.converter.UserConverter;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;
import umc6.tom.user.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * 24.07.18 작성자 : 류기현
     * 회원가입
     */
    @PostMapping("/join")
    public ApiResponse<UserDtoRes.JoinDto> join(@RequestBody UserDtoReq.JoinDto request) {
        User user = userService.join(request);

        return ApiResponse.onSuccess(UserConverter.joinRes(user));
    }


    /**
     * 24.07.18 작성자 : 류기현
     * 로그인
     */
    @PostMapping("/login")
    public JwtToken join(@RequestBody UserDtoReq.SignInDto request) {
        String name = request.getName();
        String password = request.getPassword();
        JwtToken jwtToken = userService.signIn(name, password);
        log.info("request name : {}, password : {}", name, password);
        log.info("jwtToken accessToken: {}, refreshToken : {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUserName();
    }
}
