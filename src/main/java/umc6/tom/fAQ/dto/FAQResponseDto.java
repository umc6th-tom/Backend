package umc6.tom.fAQ.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc6.tom.fAQ.model.enums.Category;

import java.time.LocalDateTime;
import java.util.List;

public class FAQResponseDto {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FAQView{
        Long fAQId;
        Category category;
        String title;
        String content;
        String updateAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FAQViewList{
        List<FAQResponseDto.FAQView> fAQList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RootFAQAddDto{
        Long fAQId;
        Category category;
        String title;
        String content;
        LocalDateTime createAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RootFAQDeleteDto{
        Long fAQId;
        LocalDateTime deletedDate;
    }

}
