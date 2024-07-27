package umc6.tom.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        String picPreview;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardLikeAddDto{
        Long boardId;
        Long userId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardDeleteDto{
        Long boardId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardComplaintDto{
        Long boardId;
        Long userId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardMyAlarmDto{
        String category;
        String alarmContent;
        String boardTitle;
        LocalDateTime alarmAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardMainViewDto{
        String title;
        Integer likeCount;
        Integer pinCount;
        Boolean isHavingPic;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardMainViewListDto{
        List<BoardMainViewDto> boardMajorList;
        List<BoardMainViewDto> boardHotList;
        List<BoardMainViewDto> boardAllList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardUpdateDto{
        Long boardId;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardViewDto{
        String userNickname;
        String userProfilePic;
        String title;
        String content;
        Integer pinCount;
        Integer likeCount;
        String boardDate; // 21분전
        List<String> boardPic;
        List<BoardViewPinListDto> pinList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardViewPinListDto{
        Long id;
        String userNickname;
        String comment;
        String pinDate;
        Integer pinLikeCount;
        Integer pinCommentCount;
        List<BoardViewPinCommentListDto> pinCommentList;
        List<String> pinPictureList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardViewPictureListDto{
        Long id;
        String pic;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardViewPinCommentListDto{
        Long id;
        String userNickname;
        String comment;
        String pinCommentDate;
        Integer pinLikeCount;
        List<String> pinPicList;
    }

}
