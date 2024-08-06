package umc6.tom.fAQ.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.FAQHandler;
import umc6.tom.fAQ.converter.FAQConverter;
import umc6.tom.fAQ.dto.FAQResponseDto;
import umc6.tom.fAQ.model.FAQ;
import umc6.tom.fAQ.model.enums.Category;
import umc6.tom.fAQ.repository.FAQRepository;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService{

    private final FAQRepository faqRepository;

    @Override
    public FAQResponseDto.FAQViewList fAQViewList(Long userId, String category, Integer page) {
        Page<FAQ> faqPage;
        if(category.equals("all"))
           faqPage = faqRepository.findAllByOrderByUpdatedAtDesc(PageRequest.of(page, 12));
        else if (category.equals("word"))
            faqPage = faqRepository.findAllByCategoryOrderByUpdatedAtDesc(Category.검색어, PageRequest.of(page, 12));
        else if (category.equals("board"))
            faqPage = faqRepository.findAllByCategoryOrderByUpdatedAtDesc(Category.커뮤니티, PageRequest.of(page, 12));
        else if (category.equals("major"))
            faqPage = faqRepository.findAllByCategoryOrderByUpdatedAtDesc(Category.문제, PageRequest.of(page, 12));
        else
            throw new FAQHandler(ErrorStatus.FAQ_CATEGORY_NOT_FOUND);

        return FAQConverter.toFAQViewList(faqPage);
    }

    @Override
    public FAQResponseDto.FAQViewList fAQSearchViewList(Long userId, String category, String content, Integer page) {
        Page<FAQ> faqPage;
        if(category.equals("all"))
            faqPage = faqRepository.findAllByTitleContainingOrderByUpdatedAtDesc
                    (content,PageRequest.of(page, 12));
        else if (category.equals("word"))
            faqPage = faqRepository.findAllByCategoryAndTitleContainingOrderByUpdatedAtDesc
                    (Category.검색어, content, PageRequest.of(page, 12));
        else if (category.equals("board"))
            faqPage = faqRepository.findAllByCategoryAndTitleContainingOrderByUpdatedAtDesc
                    (Category.커뮤니티, content, PageRequest.of(page, 12));
        else if (category.equals("major"))
            faqPage = faqRepository.findAllByCategoryAndTitleContainingOrderByUpdatedAtDesc
                    (Category.문제, content, PageRequest.of(page, 12));
        else
            throw new FAQHandler(ErrorStatus.FAQ_CATEGORY_NOT_FOUND);

        return FAQConverter.toFAQViewList(faqPage);
    }
}
