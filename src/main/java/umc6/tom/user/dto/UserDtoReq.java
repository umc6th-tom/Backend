package umc6.tom.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class UserDtoReq {


    @Getter
    @ToString
    @NoArgsConstructor
    public static class SignInDto {
        private String account;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class JoinDto {

        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        private String name;
        @Size(message = "닉네임은 2글자 이상, 10글자 이하입니다.", min = 2, max = 10)
        private String nickName;
        @NotBlank(message = "아이디는 필수! 6글자 이상, 15글자 이하입니다.")
        @Size(min = 6, max = 15)
        private String account;
        private String password;
        private String passwordCheck;   // 필요한가?
        private Long major;
        @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
        private String phone;
    }

    @Getter
    @ToString
    public static class JoinNickNameDto {

        private String nickName;
    }

    @Getter
    @ToString
    public static class WithDrawDto {

        @Size(message = "작성해주시면 사용자의 경험 향상에 도움이 됩니다.", max = 50)
        private String reason;
        @NotBlank(message = "탈퇴를 위해 비밀번호는 필수 입력 항목입니다.")
        private String password;
    }
}
