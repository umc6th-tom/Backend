package umc6.tom.gpt.dto;

import lombok.*;

public class MajorRes {

    @Getter
    @ToString
    @NoArgsConstructor
    public static class SearchDto {
        private Long id;
        private String question;
        private String questionTag;
        private String content;
        private String timer;

    }

    @Getter
    @Setter
    @Builder
    public static class getHome{
        private Long userId;
        private Long answerId;
        private Long majorId;
        private String question;
        private String content;
        private String nickname;
        private String major;
        private String createdAt;

    }
}
