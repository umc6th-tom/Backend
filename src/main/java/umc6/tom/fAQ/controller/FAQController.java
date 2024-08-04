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

    @GetMapping("/list-all")
    public ApiResponse<FAQResponseDto.FAQViewList> faqListAll(@RequestParam(name = "page") Integer page) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(faqService.listAll(userId, page));
    }

}
