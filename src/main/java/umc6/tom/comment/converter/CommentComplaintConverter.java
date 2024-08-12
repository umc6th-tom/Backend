package umc6.tom.comment.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.comment.dto.PinReportReqDto;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.comment.model.*;
import umc6.tom.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public static PinResDto.RootUserDetailPinsDto rootUserFindDetail(Long id, Long boardId, String content, LocalDateTime date){
        return PinResDto.RootUserDetailPinsDto.builder()
                .id(id)
                .boardId(boardId)
                .comment(content)
                .createdAt(date)
                .build();
    }

    public static CommentComplaintPicture toCommentComplaintPictureEntity(String picUrl, CommentComplaint commentComplaint) {
        return CommentComplaintPicture.builder()
                .pic(picUrl)
                .commentComplaint(commentComplaint)
                .build();

    }
}
