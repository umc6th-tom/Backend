package umc6.tom.firebase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import umc6.tom.alarm.converter.AlarmConverter;
import umc6.tom.alarm.model.Alarm;
import umc6.tom.alarm.model.enums.Category;
import umc6.tom.alarm.repository.AlarmRepository;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.CommentHandler;
import umc6.tom.board.model.Board;
import umc6.tom.user.model.User;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushMessage {

    private final FirebaseService firebaseService;
    private final FcmTokenService fcmTokenService;
    private final AlarmRepository alarmRepository;


    @Async
    public void commentNotification(User targetUser, Board board, String comment, Category category) {
        String alarmTitle = "새로운 댓글이 달렸어요";
        Set<String> targetFcmTokens = fcmTokenService.getAllFcmToken(targetUser.getId()).getFCmTokens();

        pushNotification(targetFcmTokens, alarmTitle, comment);

        Alarm alarm = AlarmConverter.toAlarm(board, targetUser, category, alarmTitle, comment);
        alarmRepository.save(alarm);
    }

    @Async
    public void boardLikeNotification(Board board, User likedUser) {
        String alarmTitle = likedUser.getNickName() + "님이 게시물을 좋아합니다.";
        //좋아요 취소하고 다시 하면 알림 도배 됨 방지, DB에 중복값 방지
        if(alarmRepository.existsByCategoryAndAlarmContainingAndBoardId(Category.liked, alarmTitle, board.getId()))
            return;
        Set<String> targetFcmTokens = fcmTokenService.getAllFcmToken(board.getUser().getId()).getFCmTokens();

        pushNotification(targetFcmTokens, alarmTitle, board.title);

        Alarm alarm = AlarmConverter.toAlarm(board, board.getUser(), Category.liked, alarmTitle, board.title);
        alarmRepository.save(alarm);
    }

    @Async
    public void commentLikeNotification(Board board, User targetUser, String comment, User likedUser) { //type은 board 인지 댓글인지
        String alarmTitle = likedUser.getNickName() + "님이 댓글을 좋아합니다.";
        //좋아요 취소하고 다시 하면 알림 도배 됨 방지, DB에 중복값 방지
        if(alarmRepository.existsByCategoryAndAlarmContainingAndBoardIdAndTargetAlarmContaining(Category.liked, alarmTitle, board.getId(), comment))
            return;
        Set<String> targetFcmTokens = fcmTokenService.getAllFcmToken(targetUser.getId()).getFCmTokens();

        pushNotification(targetFcmTokens, alarmTitle, comment);

        Alarm alarm = AlarmConverter.toAlarm(board, board.getUser(), Category.liked, alarmTitle, comment);
        alarmRepository.save(alarm);
    }

    private void pushNotification(Set<String> targetFcmTokens, String title, String body) {
        for (String targetFcmToken : targetFcmTokens)
            Optional.ofNullable(targetFcmToken).ifPresent(token -> {
                        try {
                            log.info("sendMessage token:" + token);
                            firebaseService.sendMessageTo(
                                    token,
                                    title,
                                    body);

                        } catch (IOException e) {
                            throw new CommentHandler(ErrorStatus.PIN_NOT_NOTIFICATION);
                        }
                    }
            );
    }


}
