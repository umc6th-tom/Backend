package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.model.Board;
import umc6.tom.comment.dto.PinDto;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.model.Pin;
import umc6.tom.user.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class PinConverter {

    public static Pin toPinEntity(User user, Board board, PinReqDto pinReq) {
        return Pin.builder()
                .board(board)
                .user(user)
                .comment(pinReq.getComment())
                .build();
    }
}
