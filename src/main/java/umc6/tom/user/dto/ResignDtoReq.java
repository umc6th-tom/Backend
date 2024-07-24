package umc6.tom.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;

public class ResignDtoReq {

    @Getter
    @Setter
    @Builder
    public static class saveDto {
        private User user;
        private LocalDateTime timer;
        private String reason;
    }
}
