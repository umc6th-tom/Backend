package umc6.tom.fAQ.service;

import umc6.tom.fAQ.dto.FAQResponseDto;

public interface FAQService {

    FAQResponseDto.FAQViewList fAQViewList(Long userId, String category, Integer page);

    FAQResponseDto.FAQViewList fAQSearchViewList(Long userId, String category, String content, Integer page);
}
