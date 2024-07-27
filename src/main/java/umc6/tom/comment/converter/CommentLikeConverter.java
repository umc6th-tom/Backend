package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.comment.model.Comment;
import umc6.tom.comment.model.CommentLike;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinLike;
import umc6.tom.user.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentLikeConverter {

    public static CommentLike toEntity(User user, Comment comment){
        return CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();
    }


}
