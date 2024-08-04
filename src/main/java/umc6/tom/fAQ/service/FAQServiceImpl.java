package umc6.tom.fAQ.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc6.tom.fAQ.converter.FAQConverter;
import umc6.tom.fAQ.dto.FAQResponseDto;
import umc6.tom.fAQ.model.FAQ;
import umc6.tom.fAQ.repository.FAQRepository;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService{

    private final FAQRepository faqRepository;

    @Override
    public FAQResponseDto.FAQViewList listAll(Long userId, Integer page) {
        Page<FAQ> faqPage = faqRepository.findByUserIdOrderByUpdatedAtDesc(userId, PageRequest.of(page, 12));

        return FAQConverter.toFAQViewList(faqPage);
    }
}
