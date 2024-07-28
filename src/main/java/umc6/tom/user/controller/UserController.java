package umc6.tom.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.SuccessStatus;
import umc6.tom.security.JwtTokenProvider;
import umc6.tom.user.converter.UserConverter;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;
import umc6.tom.user.service.UserService;

import java.util.List;

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
    public ApiResponse<UserDtoRes.JoinDto> join(@RequestBody @Valid UserDtoReq.JoinDto request) {

        User user = userService.join(request);

        return ApiResponse.onSuccess(UserConverter.joinRes(user));
    }

    /**
     * 24.07.19 작성자 : 류기현
     * 닉네임 중복 확인
     */
    @GetMapping("/nickname-dup")
    public ApiResponse<Boolean> checkNickName(@RequestBody @Valid UserDtoReq.CheckNickNameDto request) {

        return ApiResponse.onSuccess(userService.checkNickName(request));
    }

    /**
     * 24.07.19 작성자 : 류기현
     * 아이디 중복 확인
     */
    @GetMapping("/account-dup")
    public ApiResponse<Boolean> checkAccount(@RequestBody @Valid UserDtoReq.CheckAccountDto request) {

        return ApiResponse.onSuccess(userService.checkAccount(request));
    }

    /**
     * 24.07.24 작성자 : 류기현
     * 휴대폰 인증
     */
    @PostMapping("/phone-auth")
    public ApiResponse<UserDtoRes.PhoneAuthDto> phoneAuth(@RequestBody @Valid UserDtoReq.PhoneDto request) {

        return ApiResponse.onSuccess(userService.phoneAuth(request));
    }

    /**
     * 24.07.18 작성자 : 류기현
     * 로그인
     */
    @PostMapping("/login")
    public ApiResponse<UserDtoRes.LoginDto> login(@RequestBody @Valid UserDtoReq.LoginDto req,
                                                  HttpServletRequest request, HttpServletResponse response) {

        return ApiResponse.onSuccess(userService.login(request, response, req));
    }

    /**
     * 24.07.23 작성자 : 류기현
     * AccessToken 재발급
     */
    @PostMapping("/token-reissue")
    public ApiResponse<UserDtoRes.ReissueDto> reissue(@CookieValue("refreshToken") String refreshToken) {

        log.info("reissue token: {}", refreshToken);
        UserDtoRes.ReissueDto accessToken = userService.reissue(refreshToken);
        return ApiResponse.onSuccess(accessToken);
    }

    /**
     * 24.07.19 작성자 : 류기현
     * 회원 탈퇴 : userId(토큰) + 비밀번호
     */
    @PatchMapping("/withdraw")
    public ApiResponse<SuccessStatus> withdraw(@RequestBody @Valid UserDtoReq.WithDrawDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();

        userService.withDraw(userId, request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 로그아웃
     */
    @PostMapping("/logout")
    public ApiResponse<SuccessStatus> logout(@RequestHeader("Authorization") String accessToken,
                                             HttpServletRequest request, HttpServletResponse response) {

        userService.logout(request, response, accessToken);
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
    public ApiResponse<SuccessStatus> restorePhone(@RequestBody UserDtoReq.PhoneDto request) {

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

    /**
     * 24.07. 작성자 : 류기현
     * 프로필 사진 변경
     */
    @PatchMapping(value = "/pic-restore", consumes = "multipart/form-data")
    public ApiResponse<UserDtoRes.RestorePic> restorePic(@RequestPart MultipartFile request) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(userService.restorePic(userId, request));
    }

    /**
     * 24.07. 작성자 : 류기현
     * 프로필 사진 삭제(기본으로 변경)
     */
    @PatchMapping("pic-default")
    public ApiResponse<SuccessStatus> restorePicDefault() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        userService.restorePicDef(userId);

        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.23 작성자 : 류기현
     * 활동내역 공개 on/off
     */
    @PatchMapping("/agreement-change")
    public ApiResponse<UserDtoRes.ChangeAgreementDto> changeAgreement() {

        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(userService.changeAgreement(userId));
    }

    //타인 프로필 조회
    @GetMapping("/{userId}/find")
    public ApiResponse<UserDtoRes.FindProfileDto> findProfile(@PathVariable("userId") Long userId) {

        return ApiResponse.onSuccess(userService.findProfile(userId));
    }

    //활동내역 전체 조회 (내가 쓴글,댓글 단글, 좋아요)
    @GetMapping("/history")
    public ApiResponse<Page<UserDtoRes.HistoryDto>> findHistoryAll(@RequestParam(defaultValue = "1") int page,
                                                                    @PageableDefault(size = 15) Pageable pageable) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findHistoryAll(userId,adjustedPageable));
    }

    //내가 쓴글 조회
    @GetMapping("/myboards")
    public ApiResponse<Page<UserDtoRes.HistoryDto>> findMyBoards(@RequestParam(defaultValue = "1") int page,
                                                                   @PageableDefault(size = 3) Pageable pageable) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findMyBoards(userId,adjustedPageable));
    }
}
