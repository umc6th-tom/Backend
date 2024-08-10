package umc6.tom.board.dto;

import lombok.*;
import umc6.tom.board.model.BoardComplaint;
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
        Long id;
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
        Long boardComplaintId;
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
        Long id;
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
        Long id;
        Long userId; //수정, 삭제를 위한 유저 토큰id와, 게시글 유저아이디 비교용
        String userNickname;
        String userProfilePic;
        String title;
        String content;
        Integer pinCount;
        Integer likeCount;
        Boolean isLiked;
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
        Long userId; //수정, 삭제를 위한 유저 토큰id와, 게시글 유저아이디 비교용
        String userNickname;
        String comment;
        String pinDate;
        Integer pinLikeCount;
        Integer pinCommentCount;
        Boolean isLiked;
        List<String> pinPictureList;
        List<BoardViewPinCommentListDto> pinCommentList;
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
        Long userId;
        String userNickname;
        String comment;
        String pinCommentDate;
        Integer pinLikeCount;
        Boolean isLiked;
        List<String> pinCommentPicList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardComplaintPictureDto {
        BoardComplaint boardComplaint;
        String pic;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindProfileDto{
        Long id;
        String title;
        Integer likeCount;
        Integer pinCount;
    }

    @Getter
    @Setter
    @Builder
    public static class HistoryDto {
        private Long boardId;
        private String header;
        private String createdAt;
        private String title;
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class FindUserBoardsDto {
        private Long boardId;
        private String title;
        private String content;
        private String createdAt;
    }

    @Getter
    @Setter
    @Builder
    public static class HistoryCommentDto {
        private Long boardId;
        private String header;
        private String createdAt;
        private String title;
        private String comment;
    }

    @Getter
    @Setter
    @Builder
    public static class RootUserDetailDto {
        private Long boardId;
        private String title;
    }

    @Getter
    @Setter
    @Builder
    public static class RootUserReportBoardsDto {
        private Long boardId;
        private String title;
        private String content;
        private String createdAt;

        private LocalDateTime testCreatedAt;
    }

}
