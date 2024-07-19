package umc6.tom.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.SuccessStatus;
import umc6.tom.security.SecurityUtil;
import umc6.tom.security.config.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;


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
     * 24.07.19 작성자 : 류기현
     * 닉네임 중복 확인
     */
    @GetMapping("/nickname")
    public ApiResponse<Boolean> checkNickName(@RequestParam String nickname) {

        return ApiResponse.onSuccess(userService.duplicatedNickName(nickname));
    }

    /**
     * 24.07.19 작성자 : 류기현
     * 아이디 중복 확인
     */
    @GetMapping("/account")
    public ApiResponse<Boolean> checkAccount(@RequestParam String account) {

        return ApiResponse.onSuccess(userService.duplicatedAccount(account));
    }

    /**
     * 24.07.18 작성자 : 류기현
     * 로그인
     */
    @PostMapping("/login")
    public ApiResponse<UserDtoRes.signInDto> login(@RequestBody UserDtoReq.SignInDto request) {

        return ApiResponse.onSuccess(userService.signIn(request));
    }

    /**
     * 24.07.19 작성자 : 류기현
     * 회원 탈퇴 : userId(토큰) + 비밀번호
     */
    @PatchMapping("/{user_id}")
    public ApiResponse<SuccessStatus> withdraw(@RequestBody UserDtoReq.WithDrawDto request, @PathVariable String user_id) {

        userService.withDraw(1L, request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUserName();
    }
}
