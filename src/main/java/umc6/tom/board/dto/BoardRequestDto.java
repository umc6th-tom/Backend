package umc6.tom.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

public class BoardRequestDto {


    @Getter
    public static class RegisterDto {

        @NotBlank
        String title;
        @NotBlank
        String content;
        @NotBlank
        Long major;
        List<String> pic;
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
