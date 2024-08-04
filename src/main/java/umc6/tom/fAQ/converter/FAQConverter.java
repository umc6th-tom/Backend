package umc6.tom.fAQ.converter;

import org.springframework.data.domain.Page;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.fAQ.dto.FAQResponseDto;
import umc6.tom.fAQ.model.FAQ;

import java.util.List;
import java.util.stream.Collectors;

public class FAQConverter {

    public static FAQResponseDto.FAQView toFAQView(FAQ fAQ) {

        return FAQResponseDto.FAQView.builder()
                .fAQId(fAQ.getId())
                .category(fAQ.getCategory())
                .title(fAQ.getTitle())
                .content(fAQ.getContent())
                .updateAt(new DateCalc().formatDate2(fAQ.getUpdatedAt()))
                .build();
    }

    public static FAQResponseDto.FAQViewList toFAQViewList(Page<FAQ> fAQList) {
        List<FAQResponseDto.FAQView> faqViewList = fAQList.stream()
                .map(FAQConverter::toFAQView).collect(Collectors.toList());

        return FAQResponseDto.FAQViewList.builder()
                .isLast(fAQList.isLast())
                .isFirst(fAQList.isFirst())
                .totalPage(fAQList.getTotalPages())
                .totalElements(fAQList.getTotalElements())
                .listSize(faqViewList.size())
                .fAQList(faqViewList)
                .build();
    }
}
