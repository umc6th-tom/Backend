package umc6.tom.fAQ.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.fAQ.dto.FAQRequestDto;
import umc6.tom.fAQ.dto.FAQResponseDto;
import umc6.tom.fAQ.service.RootFAQService;
import umc6.tom.security.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/root")
public class RootFAQController {

    private final RootFAQService rootFAQService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 24.08.05 작성자 : 박재락
     * 자주 묻는 질문 등록
     */
    @PostMapping("/faq/register")
    public ApiResponse<FAQResponseDto.RootFAQAddDto> registerFAQ(@RequestBody FAQRequestDto.AddFAQDto request){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(rootFAQService.addRootFAQ(request, userId));
    }
    
    /**
     * 24.08.05 작성자 : 박재락
     * 자주 묻는 질문 수정
     */
    @PatchMapping("/faq/{faq-id}")
    public ApiResponse<FAQResponseDto.RootFAQAddDto> updateFAQ(@RequestBody FAQRequestDto.AddFAQDto request,
                                                               @PathVariable(name = "faq-id") Long faqId){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(rootFAQService.updateRootFAQ(request, userId, faqId));
    }

    /**
     * 24.08.05 작성자 : 박재락
     * 자주 묻는 질문 삭제
     */
    @DeleteMapping("/faq/{faq-id}")
    public ApiResponse<FAQResponseDto.RootFAQDeleteDto> updateFAQ(@PathVariable(name = "faq-id") Long faqId){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(rootFAQService.deleteRootFAQ(userId, faqId));
    }

}
