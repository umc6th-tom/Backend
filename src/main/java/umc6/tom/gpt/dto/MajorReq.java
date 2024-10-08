package umc6.tom.gpt.dto;

import lombok.*;

public class MajorReq {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class SearchDto {
        private Long majorId;
        private String question;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class exampleRegisterDto {
        private Long exampleId;
    }
}
