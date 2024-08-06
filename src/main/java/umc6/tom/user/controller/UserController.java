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
import umc6.tom.board.dto.BoardResponseDto;
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
    public ApiResponse<Boolean> checkNickName(@RequestParam(name = "nickName") String nickName) {

        return ApiResponse.onSuccess(userService.duplicatedNickName(nickName));
    }

    /**
     * 24.07.19 작성자 : 류기현
     * 아이디 중복 확인
     */
    @GetMapping("/account-dup")
    public ApiResponse<Boolean> checkAccount(@RequestParam(name = "account") String account) {

        return ApiResponse.onSuccess(userService.duplicatedAccount(account));
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
    public ApiResponse<UserDtoRes.FindAccountDto> findAccount(@RequestParam(name = "phone") String phone) {

        return ApiResponse.onSuccess(userService.findAccount(phone));
    }

    /**
     * 24.07.21 작성자 : 류기현
     * 비밀번호 찾기
     */
    @GetMapping("/find-pwd")
    public ApiResponse<UserDtoRes.FindPasswordDto> findPassword(@RequestParam(name = "phone") String phone) {

        return ApiResponse.onSuccess(userService.findPassword(phone));
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
    /**
     * 24.07.28 작성자 : 서정호
     */
    @GetMapping("/{userId}/find")
    public ApiResponse<UserDtoRes.FindProfileDto> findProfile(@PathVariable("userId") Long userId) {

        return ApiResponse.onSuccess(userService.findProfile(userId));
    }

    //타인 게시 글 조회
    /**
     * 24.07.29 작성자 : 서정호
     */
    @GetMapping("/{userId}/boards")
    public ApiResponse<Page<BoardResponseDto.FindUserBoardsDto>> findProfileBoards(@PathVariable("userId") Long userId,@RequestParam(defaultValue = "1") int page,
                                                                             @PageableDefault(size = 12) Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findProfileBoards(userId,adjustedPageable));
    }

    //타인이 댓글단 글 조회
    /**
     * 24.07.289 작성자 : 서정호
     */
    @GetMapping("/{userId}/comments")
    public ApiResponse<Page<BoardResponseDto.FindUserBoardsDto>> findProfileComments(@PathVariable("userId") Long userId,@RequestParam(defaultValue = "1") int page,
                                                                                   @PageableDefault(size = 12) Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findProfileComments(userId,adjustedPageable));
    }

    //활동내역 전체 조회 (내가 쓴글,댓글 단글, 좋아요)
    /**
     * 24.07.29 작성자 : 서정호
     */
    @GetMapping("/history")
    public ApiResponse<Page<BoardResponseDto.HistoryDto>> findHistoryAll(@RequestParam(defaultValue = "1") int page,
                                                                         @PageableDefault(size = 15) Pageable pageable) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findHistoryAll(userId,adjustedPageable));
    }

    //내가 쓴글 조회
    /**
     * 24.07.29 작성자 : 서정호
     */
    @GetMapping("/myboards")
    public ApiResponse<Page<BoardResponseDto.HistoryDto>> findMyBoards(@RequestParam(defaultValue = "1") int page,
                                                                 @PageableDefault(size = 12) Pageable pageable) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findMyBoards(userId,adjustedPageable));
    }

    //내가 쓴 댓글 글 조회
    /**
     * 24.07.29 작성자 : 서정호
     */
    @GetMapping("/mycomments")
    public ApiResponse<Page<BoardResponseDto.HistoryDto>> findMyComments(@RequestParam(defaultValue = "1") int page,
                                                                 @PageableDefault(size = 12) Pageable pageable) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findMyComments(userId,adjustedPageable));
    }

    //내가 좋아요 단 글 조회
    /**
     * 24.07.29 작성자 : 서정호
     */
    @GetMapping("/mylikes")
    public ApiResponse<Page<BoardResponseDto.HistoryDto>> findMyLikes(@RequestParam(defaultValue = "1") int page,
                                                                   @PageableDefault(size = 12) Pageable pageable) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findMyLikes(userId,adjustedPageable));
    }

    //활동내역 전체 검색 조회 (내가 쓴글,댓글 단글, 좋아요)
    /**
     * 24.08.06 작성자 : 서정호
     */
    @GetMapping("/find/history")
    public ApiResponse<Page<BoardResponseDto.HistoryDto>> findTextHistoryAll(@RequestParam(defaultValue = "1") int page,
                                                                         @PageableDefault(size = 15) Pageable pageable,
                                                                             @RequestParam(name = "content") String content ) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findTextHistoryAll(userId,adjustedPageable,content));
    }

    //활동내역 내가쓴글 검색 조회
    /**
     * 24.08.06 작성자 : 서정호
     */
    @GetMapping("/find/myboards")
    public ApiResponse<Page<BoardResponseDto.HistoryDto>> findTextHistoryBoards(@RequestParam(defaultValue = "1") int page,
                                                                             @PageableDefault(size = 15) Pageable pageable,
                                                                             @RequestParam(name = "content") String content ) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findTextHistoryBoards(userId,adjustedPageable,content));
    }

    //활동내역 댓글 검색 조회
    /**
     * 24.08.06 작성자 : 서정호
     */
    @GetMapping("/find/mycomments")
    public ApiResponse<Page<BoardResponseDto.HistoryDto>> findTextHistoryComments(@RequestParam(defaultValue = "1") int page,
                                                                                @PageableDefault(size = 15) Pageable pageable,
                                                                                @RequestParam(name = "content") String content ) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findTextHistoryComments(userId,adjustedPageable,content));
    }

    //활동내역 좋아요 검색 조회
    /**
     * 24.08.06 작성자 : 서정호
     */
    @GetMapping("/find/mylikes")
    public ApiResponse<Page<BoardResponseDto.HistoryDto>> findTextHistoryLikes(@RequestParam(defaultValue = "1") int page,
                                                                                @PageableDefault(size = 15) Pageable pageable,
                                                                                @RequestParam(name = "content") String content ) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findTextHistoryLikes(userId,adjustedPageable,content));
    }

}
