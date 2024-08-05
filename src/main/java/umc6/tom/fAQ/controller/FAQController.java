package umc6.tom.fAQ.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.fAQ.dto.FAQResponseDto;
import umc6.tom.fAQ.service.FAQService;
import umc6.tom.security.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/faq")
public class FAQController {

    private final FAQService faqService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 24.08.05 작성자 : 박재락
     * 자주 묻는 질문 전체, 검색어, 커뮤니티, 문제 리스트 조회
     */
    @GetMapping("/list-{category}")
    public ApiResponse<FAQResponseDto.FAQViewList> fAQViewList(@PathVariable(name = "category") String category,
                                                              @RequestParam(name = "page") Integer page) {
        //Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(faqService.fAQViewList(1L, category, page));
    }

    /**
     * 24.08.05 작성자 : 박재락
     * 자주 묻는 질문 전체, 검색어, 커뮤니티, 문제 리스트 검색
     */
    @GetMapping("/find-{category}")
    public ApiResponse<FAQResponseDto.FAQViewList> fAQSearchViewList(@PathVariable(name = "category") String category,
                                                                     @RequestParam(name = "content") String content,
                                                                     @RequestParam(name = "page") Integer page){
        //Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(faqService.fAQSearchViewList(1L, category, content, page));
    }
}
