package umc6.tom.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.SuccessStatus;
import umc6.tom.security.JwtTokenProvider;
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
    public ApiResponse<UserDtoRes.SignInDto> login(@RequestBody UserDtoReq.SignInDto request) {

        return ApiResponse.onSuccess(userService.signIn(request));
    }

    /**
     * 24.07.19 작성자 : 류기현
     * 회원 탈퇴 : userId(토큰) + 비밀번호
     */
    @PatchMapping("/widthdraw")
    public ApiResponse<SuccessStatus> withdraw(@RequestBody UserDtoReq.WithDrawDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();

        userService.withDraw(userId, request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 로그아웃
     */
    @PostMapping("/logout")
    public ApiResponse<SuccessStatus> logout(@RequestHeader("Authorization") String accessToken) {

        userService.logout(accessToken);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 아이디 찾기
     */
    @GetMapping("/find-id")
    public ApiResponse<UserDtoRes.FindAccountDto> findAccount(@RequestBody UserDtoReq.FindAccountDto request) {

        return ApiResponse.onSuccess(userService.findAccount(request));
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 비밀번호 찾기
     */
    @GetMapping("/find-pwd")
    public ApiResponse<UserDtoRes.FindPasswordDto> findPassword(@RequestBody UserDtoReq.FindPasswordDto request) {

        return ApiResponse.onSuccess(userService.findPassword(request));
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 비밀번호 찾기 후 재설정
     */
    @PatchMapping("/find-pwd-restore")
    public ApiResponse<SuccessStatus> restorePassword(@RequestBody UserDtoReq.FindRestorePasswordDto request) {

        userService.findRestorePassword(request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 아이디 재설정
     */
    @PatchMapping("/id-restore")
    public ApiResponse<SuccessStatus> restoreAccount(@RequestBody UserDtoReq.RestoreAccountDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();

        userService.restoreAccount(userId, request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 비밀번호 재설정
     */
    @PatchMapping("/pwd-restore")
    public ApiResponse<SuccessStatus> restorePassword(@RequestBody UserDtoReq.RestorePasswordDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();

        userService.restorePassword(userId, request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 닉네임 재설정
     */
    @PatchMapping("/nickname-restore")
    public ApiResponse<SuccessStatus> restoreNickName(@RequestBody UserDtoReq.RestoreNickNameDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();

        userService.restoreNickName(userId, request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 휴대폰 번호 재설정
     */
    @PatchMapping("/phone-restore")
    public ApiResponse<SuccessStatus> restorePhone(@RequestBody UserDtoReq.RestorePhoneDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();

        userService.restorePhone(userId, request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 전공 재설정
     */
    @PatchMapping("/major-restore")
    public ApiResponse<SuccessStatus> restoreMajor(@RequestBody UserDtoReq.RestoreMajorDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();

        userService.restoreMajor(userId, request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }
}
