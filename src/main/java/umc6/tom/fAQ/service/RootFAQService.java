package umc6.tom.fAQ.service;

import umc6.tom.fAQ.dto.FAQRequestDto;
import umc6.tom.fAQ.dto.FAQResponseDto;

public interface RootFAQService {
    FAQResponseDto.RootFAQAddDto addRootFAQ(FAQRequestDto.AddFAQDto request, Long userId);

    FAQResponseDto.RootFAQAddDto updateRootFAQ(FAQRequestDto.AddFAQDto request, Long userId, Long fqaId);

    FAQResponseDto.RootFAQDeleteDto deleteRootFAQ(Long userId, Long fqaId);
}
