package umc6.tom.notice.dto;

import lombok.Getter;
import lombok.Setter;

public class NoticeReq {

    @Getter
    @Setter
    public static class registerDto{

        private String title;
        private String content;
    }
}
