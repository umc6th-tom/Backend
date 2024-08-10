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
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.service.UserService;
import org.springframework.data.domain.Page;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/root/user")
public class RootUserController {

    private final UserService userService;

    /**
     * 24.08.07 작성자 : 서정호
     * 회원 검색 (전체) + 페이징
     */
    @GetMapping("/find/all")
    public ApiResponse<Page<UserDtoRes.userFindAllDto>> findAllUser(@RequestParam(name = "keyword") String keyword,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @PageableDefault(size = 12) Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        return ApiResponse.onSuccess(userService.findAllUser(keyword,adjustedPageable));
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
        return ApiResponse.onSuccess(userService.findNicknameUser(keyword,adjustedPageable));
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
        return ApiResponse.onSuccess(userService.findNameUser(keyword,adjustedPageable));
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
        return ApiResponse.onSuccess(userService.findAccountUser(keyword,adjustedPageable));
    }

    /**
     * 24.08.07 작성자 : 서정호
     * 회원 상세 보기
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserDtoRes.userFindDetailDto> findUserDetail(@PathVariable(name = "userId") Long userId){
        return ApiResponse.onSuccess(userService.findUserDetail(userId));
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
        return ApiResponse.onSuccess(userService.findUserReportBoards(boardUserId,adjustedPageable));
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
        return ApiResponse.onSuccess(userService.findUserReportPins(pinUserId,adjustedPageable));
    }



}
