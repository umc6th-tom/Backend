package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.model.Board;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinPicture;
import umc6.tom.user.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class PinPictureConverter {

    public static PinPicture toPinPictureEntity(String picUrl,Pin pin) {
        return PinPicture.builder()
                .pic(picUrl)
                .pin(pin)
                .build();

    }


}
