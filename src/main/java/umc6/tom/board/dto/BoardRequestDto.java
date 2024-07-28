package umc6.tom.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class BoardRequestDto {


    @Getter
    public static class RegisterDto {

        @NotBlank
        String title;
        @NotBlank
        String content;
        @NotNull
        Long majorId;
    }

    @Getter
    public static class AddComplaintDto {
        @NotBlank
        String content;
    }

    @Getter
    public static class UpdateBoardDto {
        @NotBlank
        String title;
        String content;
        List<String> pic;
    }
}
