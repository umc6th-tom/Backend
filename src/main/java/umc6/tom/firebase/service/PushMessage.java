package umc6.tom.firebase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.apiPayload.exception.handler.CommentHandler;
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

    @Async
    public void pinNotification(User boardUser, User pinUser, String title, String comment) {
        log.info("댓글알림");
        Set<String> targetFcmTokens = fcmTokenService.getAllFcmToken(boardUser.getId()).getFCmTokens();
        for (String targetFcmToken : targetFcmTokens)
            Optional.ofNullable(targetFcmToken).ifPresent(token -> {
                        try {
                            log.info(token);
                            firebaseService.sendMessageTo(
                                    token,
                                    "new comment!",
                                    boardUser.getName() + "of " + title + "board " + pinUser.getNickName() + "commented.");
                        } catch (IOException e) {
                            throw new CommentHandler(ErrorStatus.PIN_NOT_NOTIFICATION);
                        }
                    }
            );

    }
}
