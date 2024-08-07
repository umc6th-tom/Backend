package umc6.tom.inquiry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class QNARequestDto {

    @Getter
    public static class RegisterDto {
        @NotBlank
        String title;
        @NotBlank
        String content;
    }

    @Getter
    public static class AnswerDto {
        @NotBlank
        String answer;
    }
}
