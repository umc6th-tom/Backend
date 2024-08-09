package umc6.tom.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.notice.dto.NoticeReq;
import umc6.tom.notice.service.NoticeService;
import umc6.tom.security.JwtTokenProvider;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/root/notice")
public class RootNoticeController {

    private final NoticeService noticeService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 24.08.10 작성자 : 서정호
     * 공지사항 등록하기
     */
    @PostMapping("/register")
    public ApiResponse register(@RequestBody NoticeReq.registerDto registerDto){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        if(noticeService.register(registerDto, userId)){
            return ApiResponse.onSuccess(200);
        }else {
            return ApiResponse.onFailure("502","등록에 실패했습니다.",registerDto);
        }
    }

    /**
     * 24.08.10 작성자 : 서정호
     * 공지사항 삭제하기
     */
    @DeleteMapping("/{noticeId}/delete")
    public ApiResponse delete(@PathVariable("noticeId") Long noticeId){

        if(noticeService.delete(noticeId)){
            return ApiResponse.onSuccess(200);
        }else {
            return ApiResponse.onFailure("502","삭제에 실패했습니다.", noticeId);
        }
    }

    /**
     * 24.08.10 작성자 : 서정호
     * 공지사항 수정하기
     */
    @PatchMapping("/{noticeId}/update")
    public ApiResponse update(@PathVariable("noticeId") Long noticeId, @RequestBody NoticeReq.registerDto req){

        Long userId = jwtTokenProvider.getUserIdFromToken();

        if(noticeService.update(noticeId,req,userId)){
            return ApiResponse.onSuccess(200);
        }else {
            return ApiResponse.onFailure("502","삭제에 실패했습니다.", req);
        }
    }
}
