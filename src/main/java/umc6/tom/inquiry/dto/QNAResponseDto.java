package umc6.tom.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class QNAResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterResultDto{
        Long qnaId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QNAListViewDto{
        Long id;
        String status;
        String title;
        String createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QNAListViewListDto{
        List<QNAResponseDto.QNAListViewDto> qnaList;
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
    public static class QNAViewDto{
        Long id;
        String title;
        String content;
        String answer;
        List<String> picList;
        String createdAt;
        String answeredAt;
    }

}
