package umc6.tom.firebase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class FCMRequestDto {

    @Getter
    public static class AlarmPushDto {

        @NotBlank
        String targetToken;
        @NotBlank
        String title;
        @NotBlank
        String body;
    }
}
