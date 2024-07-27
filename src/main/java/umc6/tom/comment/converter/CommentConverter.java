package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.model.Board;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.comment.model.Comment;
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

    public static PinResDto toPinDto(Pin pin) {
        List<String> pictureUrls = pin.getPinPictureList().stream()
                .map(PinPicture::getPic)
                .toList();

        return PinResDto.builder()
                .id(pin.getId())
                .boardId(pin.getBoard().getId())
                .userId(pin.getUser().getId())
                .comment(pin.getComment())
                .pic(pictureUrls)
                .build();
    }
}
