package umc6.tom.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.comment.dto.PinReportReqDto;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.service.PinService;
import umc6.tom.security.JwtTokenProvider;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pin")
public class PinController {

    private final PinService pinService;
    private final JwtTokenProvider jwtTokenProvider;

    //댓글 등록
    //댓글과 이미지를 받아오면 됨 + 토큰으로 유저 ID
    @PostMapping(value = "/{boardId}/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse register(@PathVariable("boardId") Long boardId,
                                @RequestPart @Valid PinReqDto.PinCommentAndPic request,
                                @RequestPart(required = false) MultipartFile[] files ) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return pinService.pinRegister(request,boardId,userId,files);
    }

    //댓글 조회
    @GetMapping("/detail/{pinId}")
    public ApiResponse detail(@PathVariable("pinId") Long pinId) {
        return pinService.getDetailPin(pinId);
    }

    //댓글 수정
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse modify(@RequestPart @Valid PinReqDto.PinAndPic request,
                              @RequestPart(required = false) MultipartFile[] files) {

        return pinService.pinModify(request,files);
    }

    //댓글 삭제
    @DeleteMapping("/{pinId}")
    public ApiResponse modify(@PathVariable("pinId") Long pinId) {

        return pinService.pinDelete(pinId);
    }

    //댓글 좋아요/제거
    @PostMapping("/like/{pinId}")
    public ApiResponse like(@PathVariable("pinId") Long pinId) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return pinService.pinLikeSet(pinId,userId);
    }

    //댓글 신고
    @PostMapping("/report/{pinId}")
    public ApiResponse report(@PathVariable("pinId") Long pinId, @RequestBody PinReportReqDto.PinReportDto reportDto) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return pinService.pinReport(pinId,reportDto,userId);
    }

}
