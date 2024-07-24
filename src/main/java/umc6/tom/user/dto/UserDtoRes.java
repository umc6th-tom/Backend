package umc6.tom.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc6.tom.board.model.Board;

import java.time.LocalDateTime;
import java.util.List;

public class UserDtoRes {

    @Getter
    @Setter
    @Builder
    public static class JoinDto {

        private Long id;
        private String nickName;
    }

    @Setter
    @Getter
    @Builder
    public static class PhoneAuthDto {

        private String phone;
        private String authNum;
    }

    @Getter
    @Setter
    @Builder
    public static class LoginDto {

        private Long userId;
        private String accessToken;
        private String refreshToken;
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

    @Getter
    @Setter
    @Builder
    public static class ChangeAgreementDto {

        private String agreement;
    }

    @Getter
    @Setter
    @Builder
    public static class ProfileDto {

        private Long userId;
        private String pic;
        private String nickName;
        private String description;
        private String agreement;
        private List<Board> written;
        private List<Board> commented;
        private String message;
    }
}
