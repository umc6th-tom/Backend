package umc6.tom.fAQ.service;

import umc6.tom.fAQ.dto.FAQResponseDto;

public interface FAQService {

    FAQResponseDto.FAQViewList listAll(Long userId, Integer page);
}
