package umc6.tom.kakao.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.kakao.service.KakaoService;
import umc6.tom.user.converter.UserConverter;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;
import umc6.tom.user.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    private final UserService userService;


/*    @GetMapping("/callback")
    public ApiResponse<?> callback(@RequestParam("code") String code) {

        return ApiResponse.onSuccess(code);
    }*/

    /**
     * 24.08.15 작성자 : 류기현
     * 카카오 토큰 응답
     */
    @GetMapping("/oauth/kakao")
    public ApiResponse<String> kakaoLogin(@RequestParam("code") String code) {

        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        return ApiResponse.onSuccess(accessToken);
    }

    /**
     * 24.08.15 작성자 : 류기현
     * 카카오 회원가입 전단계
     */
    @GetMapping("/oauth/kakao/user")
    public ApiResponse<UserDtoRes.kakaoJoinDto> joinKakao(@RequestHeader("Authorization") String accessToken) {

        return ApiResponse.onSuccess(kakaoService.kakaoUserInfo(accessToken));
    }

    /**
     * 24.08.15 작성자 : 류기현
     * 카카오 회원가입, 계정 통합
     */
    @PostMapping("/oauth/kakao/join")
    public ApiResponse<UserDtoRes.JoinDto> joinKakao(@RequestHeader("Authorization") String accessToken,
                                                     @RequestBody UserDtoReq.JoinDto request) {

         UserDtoRes.kakaoJoinDto kakaoUserInfo = kakaoService.kakaoUserInfo(accessToken);

        User user = userService.joinKakao(kakaoUserInfo, request);
        return ApiResponse.onSuccess(UserConverter.joinRes(user));
    }
}
