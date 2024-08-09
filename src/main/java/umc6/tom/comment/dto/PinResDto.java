package umc6.tom.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


public class PinResDto {

    @Getter
    @Setter
    @Builder
    public static class DetailPin {
        private Long id;
        private Long userId;
        private Long boardId;
        private String comment;
        private List<String> pic;
    }

    @Getter
    @Setter
    @Builder
    public static class RootUserDetailPinsDto {
        private Long id;
        private Long boardId;
        private String comment;
        private LocalDateTime createdAt;
    }

}
