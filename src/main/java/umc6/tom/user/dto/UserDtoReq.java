package umc6.tom.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class UserDtoReq {


    @Getter
    @ToString
    @NoArgsConstructor
    public static class SignInDto {
        private String name;
        private String password;
    }
}
