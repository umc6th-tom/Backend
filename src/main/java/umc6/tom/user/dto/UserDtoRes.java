package umc6.tom.user.dto;

import jakarta.servlet.http.Cookie;
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
    public static class LoginDto {

        private Long userId;
        private String accessToken;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    public static class ReissueDto {

        private String accessToken;
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
