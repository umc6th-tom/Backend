package umc6.tom.pin.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.pin.model.Pin;
import umc6.tom.pin.model.PinPicture;

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
