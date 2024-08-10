package umc6.tom.notice.converter;

import umc6.tom.notice.dto.NoticeReq;
import umc6.tom.notice.model.Notice;
import umc6.tom.user.model.User;

public class NoticeConverter {

    public static Notice toNoticeRegitser(NoticeReq.registerDto registerDto, User user){
        return Notice.builder()
                .user(user)
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .build();
    }

    public static Notice toNoticeUpdate(NoticeReq.registerDto registerDto, Long noticeId, User user){
        return Notice.builder()
                .id(noticeId)
                .user(user)
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .build();
    }

}
