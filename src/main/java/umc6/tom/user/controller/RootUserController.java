package umc6.tom.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.security.JwtTokenProvider;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.service.RootUserService;
import umc6.tom.user.service.UserService;
import org.springframework.data.domain.Page;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/root/user")
public class RootUserController {

    private final UserService userService;
    private final RootUserService rootUserService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 24.08.07 작성자 : 서정호
     * 회원 검색 (전체) + 페이징
     */
    @GetMapping("/find/all")
    public ApiResponse<Page<UserDtoRes.userFindAllDto>> findAllUser(@RequestParam(name = "keyword") String keyword,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @PageableDefault(size = 12) Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(rootUserService.findAllUser(keyword,adjustedPageable));
    }

    /**
     * 24.08.07 작성자 : 서정호
     * 회원 검색 (닉네임) + 페이징
     */
    @GetMapping("/find/nickname")
    public ApiResponse<Page<UserDtoRes.userFindAllDto>> findNicknameUser(@RequestParam(name = "keyword") String keyword,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @PageableDefault(size = 12) Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(rootUserService.findNicknameUser(keyword,adjustedPageable));
    }

    /**
     * 24.08.07 작성자 : 서정호
     * 회원 검색 (이름) + 페이징
     */
    @GetMapping("/find/name")
    public ApiResponse<Page<UserDtoRes.userFindAllDto>> findNameUser(@RequestParam(name = "keyword") String keyword,
                                                                         @RequestParam(defaultValue = "1") int page,
                                                                         @PageableDefault(size = 12) Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(rootUserService.findNameUser(keyword,adjustedPageable));
    }

    /**
     * 24.08.07 작성자 : 서정호
     * 회원 검색 (이름) + 페이징
     */
    @GetMapping("/find/account")
    public ApiResponse<Page<UserDtoRes.userFindAllDto>> findAccountUser(@RequestParam(name = "keyword") String keyword,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @PageableDefault(size = 12) Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(rootUserService.findAccountUser(keyword,adjustedPageable));
    }

    /**
     * 24.08.07 작성자 : 서정호
     * 회원 상세 보기
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserDtoRes.userFindDetailDto> findUserDetail(@PathVariable(name = "userId") Long userId){
        return ApiResponse.onSuccess(rootUserService.findUserDetail(userId));
    }

    /**
     * 24.08.10 작성자 : 서정호
     * 유저 신고된 글 리스트 조회 + 페이징
     */
    @GetMapping("/{userId}/boards")
    public ApiResponse<Page<BoardResponseDto.RootUserReportBoardsDto>> findUserReportBoards(@PathVariable(name = "userId") Long boardUserId,
                                                                                            @RequestParam(defaultValue = "1") int page,
                                                                                            @PageableDefault(size = 12) Pageable pageable){
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(rootUserService.findUserReportBoards(boardUserId,adjustedPageable));
    }

    /**
     * 24.08.10 작성자 : 서정호
     * 유저 신고된 댓글 리스트 조회 + 페이징
     */
    @GetMapping("/{userId}/pins")
    public ApiResponse<Page<PinResDto.RootUserReportPinsOrCommentsPinsDto>> findUserReportPins(@PathVariable(name = "userId") Long pinUserId,
                                                                                               @RequestParam(defaultValue = "1") int page,
                                                                                               @PageableDefault(size = 6) Pageable pageable){
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(rootUserService.findUserReportPins(pinUserId,adjustedPageable));
    }


    /**
     * 24.08.07 작성자 : 류기현
     * 관리자 - 경고 부여
     */
    @PostMapping("/warn")
    public ApiResponse<UserDtoRes.warnDto> warn(@RequestBody UserDtoReq.WarnDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(rootUserService.warn(userId, request));
    }

    /**
     * 24.08.07 작성자 : 류기현
     * 관리자 - 회원 정지
     */
    @PostMapping("/suspension")
    public ApiResponse<UserDtoRes.suspensionDto> suspension(@RequestBody UserDtoReq.SuspensionDto request) {

        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(rootUserService.suspension(userId, request));
    }

    /**
     * 24.08.13 작성자 : 서정호
     * 관리자 - 유저 신고된 글 조회
     */
    @GetMapping("/boardcomplaint/{complaintId}")
    public ApiResponse<UserDtoRes.complaintBoardReasonDto> BoardReportReason(@PathVariable("complaintId") Long complaintId) {
//        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(rootUserService.boardReportReason(complaintId));
    }

    /**
     * 24.08.14 작성자 : 서정호
     * 관리자 - 유저 신고된 댓글 조회
     */
    @GetMapping("/pincomplaint/{pinComplaintId}")
    public ApiResponse<UserDtoRes.complaintCommentReasonDto> pinReportReason(@PathVariable("pinComplaintId") Long complaintId) {
//        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(rootUserService.pinReportReason(complaintId));
    }

    /**
     * 24.08.14 작성자 : 서정호
     * 관리자 - 유저 신고된 대댓글 조회
     */
    @GetMapping("/commentcomplaint/{commentComplaintId}")
    public ApiResponse<UserDtoRes.complaintCommentReasonDto> commentReportReason(@PathVariable("commentComplaintId") Long complaintId) {
//        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(rootUserService.commentReportReason(complaintId));
    }
    /**
     * 24.08.16 작성자 : 서정호
     * 관리자 - 유저 신고된 전체 글 조회
     */
    @GetMapping("/complaints/all")
    public ApiResponse<Page<UserDtoRes.complaintAllResDto>> complaintsAll(@RequestParam(defaultValue = "1") int page,
                                                                           @PageableDefault(size = 12) Pageable pageable) {
//        Long userId = jwtTokenProvider.getUserIdFromToken();
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(rootUserService.complaintsAll(adjustedPageable));
    }
}
