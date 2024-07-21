package umc6.tom.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserDtoRes {

    @Getter
    @Setter
    @Builder
    public static class JoinDto {
        private Long id;
        private String nickName;
    }

    @Getter
    @Setter
    @Builder
    public static class SignInDto {
        private Long userId;
        private String accessToken;
        private String refreshToken;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    public static class FindAccountDto {
        private String account;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    public static class FindPasswordDto {
        private Long userId;
    }
}
