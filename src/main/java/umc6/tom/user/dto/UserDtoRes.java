package umc6.tom.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserDtoRes {

    @Getter
    @Setter
    @Builder
    public static class JoinDto {
        private Long id;
        private String nickName;
    }
}
