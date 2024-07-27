package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.comment.dto.PinReportReqDto;
import umc6.tom.comment.model.Comment;
import umc6.tom.comment.model.CommentComplaint;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinComplaint;
import umc6.tom.user.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentComplaintConverter {


    public static CommentComplaint toCommentComplaintEntity(PinReportReqDto.PinReportDto reportDto, User user, Comment comment) {
        return CommentComplaint.builder()
                .comment(comment)
                .user(user)
                .content(reportDto.getContent())
                .commentUserId(comment.getUser().getId())
                .commentComment(comment.getComment())
                .build();
    }


}
