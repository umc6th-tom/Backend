package umc6.tom.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
}
