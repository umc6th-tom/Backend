package umc6.tom.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class MajorReq {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class SearchDto {
        private String question;
    }
}
