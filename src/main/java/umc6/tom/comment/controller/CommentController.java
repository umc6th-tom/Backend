package umc6.tom.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.comment.dto.PinReportReqDto;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.service.CommentService;
import umc6.tom.security.JwtTokenProvider;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService pinService;
    private final JwtTokenProvider jwtTokenProvider;

    //댓글 등록
    //댓글과 이미지를 받아오면 됨 + 토큰으로 유저 ID
    @PostMapping("/{boardId}/register")
    public ApiResponse register(@PathVariable("boardId") Long boardId,@RequestBody PinReqDto.PinCommentAndPic pinReqDto ) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return pinService.pinRegister(pinReqDto,boardId,userId);
    }

    //댓글 조회
    @GetMapping("/detail/{commentId}")
    public ApiResponse detail(@PathVariable("commentId") Long commentId) {
        return pinService.getDetailPin(commentId);
    }

    //댓글 수정
    @PatchMapping("/update")
    public ApiResponse modify(@RequestBody PinReqDto.PinAndPic pinDto) {

        return pinService.pinModify(pinDto);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse modify(@PathVariable("commentId") Long commentId) {

        return pinService.pinDelete(commentId);
    }

    //댓글 좋아요/제거
    @PostMapping("/like/{commentId}")
    public ApiResponse like(@PathVariable("commentId") Long commentId) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return pinService.pinLikeSet(commentId,userId);
    }

    //댓글 신고
    @PostMapping("/report/{commentId}")
    public ApiResponse report(@PathVariable("commentId") Long commentId, @RequestBody PinReportReqDto.PinReportDto reportDto) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return pinService.pinReport(commentId,reportDto,userId);
    }





}
