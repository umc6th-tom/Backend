package umc6.tom.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc6.tom.board.model.Board;
import umc6.tom.comment.model.Pin;

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
