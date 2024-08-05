package umc6.tom.fAQ.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import umc6.tom.fAQ.model.enums.Category;

public class FAQRequestDto {

    @Getter
    public static class AddFAQDto {
        @NotBlank
        Category category;
        @NotBlank
        String title;
        @NotBlank
        String content;
    }

    @Getter
    public static class UpdateFAQDto {
        @NotBlank
        Category category;
        @NotBlank
        String title;
        @NotBlank
        String content;
    }
}
