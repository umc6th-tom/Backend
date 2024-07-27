package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.model.Board;
import umc6.tom.comment.dto.CommentDto;
import umc6.tom.comment.dto.CommentResDto;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.comment.model.Comment;
import umc6.tom.comment.model.CommentPicture;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinPicture;
import umc6.tom.user.model.User;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentConverter {

    public static Pin toPinUpdateEntity(PinReqDto.PinAndPic pinReqDto) {
        return Pin.builder()
                .id(pinReqDto.getId())
                .comment(pinReqDto.getComment())
                .build();
    }

    public static Comment toCommentEntity(User user, Pin pin, PinReqDto.PinCommentAndPic pinReq) {
        return Comment.builder()
                .pin(pin)
                .user(user)
                .comment(pinReq.getComment())
                .build();
    }

    public static CommentResDto toCommentDto(Comment comment) {
        List<String> pictureUrls = comment.getCommentPictureList().stream()
                .map(CommentPicture::getPic)
                .toList();

        return CommentResDto.builder()
                .id(comment.getId())
                .pinId(comment.getPin().getId())
                .userId(comment.getUser().getId())
                .comment(comment.getComment())
                .pic(pictureUrls)
                .build();
    }
}
