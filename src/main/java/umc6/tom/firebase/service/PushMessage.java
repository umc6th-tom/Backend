package umc6.tom.firebase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import umc6.tom.alarm.converter.AlarmConverter;
import umc6.tom.alarm.model.Alarm;
import umc6.tom.alarm.model.enums.Field;
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
    public void commentNotification(User alarmUser, Board board, Field field) {
        log.info("userID:"+alarmUser.getId()+"에게 알림");
        Set<String> targetFcmTokens = fcmTokenService.getAllFcmToken(alarmUser.getId()).getFCmTokens();
        String alarmTitle = "새로운 댓글이 달렸어요";
        for (String targetFcmToken : targetFcmTokens)
            Optional.ofNullable(targetFcmToken).ifPresent(token -> {
                        try {
                            log.info("sendMessage token:"+token);
                            firebaseService.sendMessageTo(
                                    token,
                                    alarmTitle,
                                    board.getTitle());

                        } catch (IOException e) {
                            throw new CommentHandler(ErrorStatus.PIN_NOT_NOTIFICATION);
                        }
                    }
            );
        Alarm alarm = AlarmConverter.toAlarm(board, field, alarmTitle);
        alarmRepository.save(alarm);
    }

    @Async
    public void boardLikeNotification(Board board, User likedUser) {
        log.info("userID:"+board.getUser().getId()+"에게 알림");
        Set<String> targetFcmTokens = fcmTokenService.getAllFcmToken(board.getUser().getId()).getFCmTokens();
        String alarmTitle = likedUser.getNickName() + "님이 게시물을 좋아합니다.";
        for (String targetFcmToken : targetFcmTokens)
            Optional.ofNullable(targetFcmToken).ifPresent(token -> {
                        try {
                            log.info("sendMessage token:" + token);
                            firebaseService.sendMessageTo(
                                    token,
                                    alarmTitle,
                                    board.getTitle());

                        } catch (IOException e) {
                            throw new CommentHandler(ErrorStatus.PIN_NOT_NOTIFICATION);
                        }
                    }
            );
        Alarm alarm = AlarmConverter.toAlarm(board, Field.liked, alarmTitle);
        alarmRepository.save(alarm);
    }

}
