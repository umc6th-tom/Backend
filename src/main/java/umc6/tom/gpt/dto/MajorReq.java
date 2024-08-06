package umc6.tom.gpt.dto;

import lombok.*;

public class MajorReq {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class SearchDto {
        private String question;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @ToString
    public static class exampleRegisterDto {
        private String tag;
        private String exampleQuestion;
        private String correctAnswer;
    }
}
