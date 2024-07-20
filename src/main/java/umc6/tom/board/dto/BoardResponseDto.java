package umc6.tom.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BoardResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterResultDto{
        Long boardId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardListViewDto{
        String title;
        String content;
        Integer pinCount;
        Integer likeCount;
        String boardDate; // 21분전
        String userNickName;
        //사진 구현 필요
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardListViewListDto{
        List<BoardListViewDto> boardList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }


}
