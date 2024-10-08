package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.board.model.BoardComplaintPicture;
import umc6.tom.comment.dto.PinReportReqDto;
import umc6.tom.comment.model.*;
import umc6.tom.user.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class PinComplaintConverter {


    public static PinComplaint toPinComplaintEntity(PinReportReqDto.PinReportDto reportDto, User user, Pin pin) {
        return PinComplaint.builder()
                .pin(pin)
                .user(user)
                .content(reportDto.getContent())
                .pinUserId(pin.getUser().getId())
                .pinComment(pin.getComment())
                .build();
    }

    public static PinComplaintPicture toPinComplaintPictureEntity(String picUrl, PinComplaint pinComplaint) {
        return PinComplaintPicture.builder()
                .pic(picUrl)
                .pinComplaint(pinComplaint)
                .build();

    }

}
