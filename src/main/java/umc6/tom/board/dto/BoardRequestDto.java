package umc6.tom.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class BoardRequestDto {


    @Getter
    public static class RegisterDto {

        @NotBlank
        String title;
        @NotBlank
        String content;
        @NotBlank
        Long major;
        //사진 구현 필요
    }

    @Getter
    public static class AddComplaintDto {

        @NotBlank
        String content;
    }
}
