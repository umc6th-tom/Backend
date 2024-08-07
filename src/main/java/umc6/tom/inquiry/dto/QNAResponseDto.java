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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerQNADto{
        Long qnaId;
        Long answeredAdminId;
        String answer;
        LocalDateTime answeredAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteQNAAnswerDto{
        Long qnaId;
        Long answeredAdminId;
        LocalDateTime deletedAnswerAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RootQNAListViewDto{
        Long id;
        String status;
        String title;
        String content;
        String createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RootQNAListViewListDto{
        List<QNAResponseDto.RootQNAListViewDto> rootQNAList;
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
    public static class RootQNAViewDto{
        Long id;
        Long userId;
        String userNickname;
        String userprofile;
        String createdAt;
        String title;
        String content;
        List<String> picList;
        String answer;
    }
}
