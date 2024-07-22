package umc6.tom.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.comment.dto.PinDto;
import umc6.tom.comment.dto.PinPictureDto;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinPicture;
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
    public ApiResponse register(@PathVariable("boardId") Long boardId,@RequestBody PinReqDto pinReqDto) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return pinService.pinRegister(pinReqDto,boardId,userId);
    }
}
