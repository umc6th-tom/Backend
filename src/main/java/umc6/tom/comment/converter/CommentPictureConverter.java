package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.comment.dto.PinReportReqDto;
import umc6.tom.comment.model.*;
import umc6.tom.user.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentPictureConverter {


    public static CommentPicture toCommentPictureEntity(String picUrl, Comment comment) {
        return CommentPicture.builder()
                .pic(picUrl)
                .comment(comment)
                .build();

    }


}
