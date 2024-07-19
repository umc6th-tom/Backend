package umc6.tom.user.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.user.dto.ResignDtoReq;
import umc6.tom.user.model.Resign;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResignConverter {

    public static Resign toResign(ResignDtoReq.saveDto request) {

        return Resign.builder()
                .user(request.getUser())
                .timer(request.getTimer())
                .reason(request.getReason())
                .build();
    }
}
