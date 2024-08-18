package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardPicture;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.comment.model.*;
import umc6.tom.common.model.Uuid;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PinConverter {

    public static List<String> toPicStringIdList(List<PinPicture> pinPictureList){
        List<String> picList = new ArrayList<>();
        for(PinPicture pinPicture : pinPictureList){
            picList.add(pinPicture.getPic());
        }
        return picList;
    }

    public static Pin toPinUpdateEntity(PinReqDto.PinAndPic pinReqDto) {
        return Pin.builder()
                .id(pinReqDto.getId())
                .comment(pinReqDto.getComment())
                .build();
    }

    public static Pin toPinEntity(User user, Board board, PinReqDto.PinCommentAndPic pinReq) {
        return Pin.builder()
                .board(board)
                .user(user)
                .comment(pinReq.getComment())
                .report(0)
                .build();
    }

    public static PinResDto.DetailPin toPinDto(Pin pin) {
        List<String> pictureUrls = pin.getPinPictureList().stream()
                .map(PinPicture::getPic)
                .toList();

        return PinResDto.DetailPin.builder()
                .id(pin.getId())
                .boardId(pin.getBoard().getId())
                .userId(pin.getUser().getId())
                .comment(pin.getComment())
                .pic(pictureUrls)
                .build();
    }

    public static PinResDto.RootUserReportPinsOrCommentsPinsDto rootUserReportPinsOrCommentsPinsDto(Long complaintId , String content, LocalDateTime createdAt, String what){



        return PinResDto.RootUserReportPinsOrCommentsPinsDto.builder()
                .complaintId(complaintId)
                .comment(content)
                .createdAt(DateCalc.formatDate2(createdAt))
                .pinOrComment(what)
                .build();
    }

    public static PinPicture toPinPicture(Pin pin, String pic){
        return PinPicture.builder()
                .pin(pin)
                .pic(pic)
                .build();
    }

    public static CommentPicture toCommentPicture(Comment comment, String pic){
        return CommentPicture.builder()
                .comment(comment)
                .pic(pic)
                .build();
    }
}
