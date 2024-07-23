package umc6.tom.alarm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc6.tom.alarm.model.enums.AlarmOnOff;
import umc6.tom.alarm.service.AlarmSetService;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.SuccessStatus;
import umc6.tom.security.JwtTokenProvider;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AlarmSetController {

    private final AlarmSetService alarmSetService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 24.07.24 작성자 : 류기현
     * 알림 전체 켜기
     */
    @PatchMapping("/all-change")
    public ApiResponse<SuccessStatus> changeAll() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        alarmSetService.alarmSetAllChange(userId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.07.24 작성자 : 류기현
     * 댓글 알람 켜기/끄기
     */
    @PatchMapping("/pin-change")
    public ApiResponse<AlarmOnOff> changePin() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(alarmSetService.pinAlarmChange(userId));
    }

    /**
     * 24.07.24 작성자 : 류기현
     * 대댓글 소식 알람 켜기/끄기
     */
    @PatchMapping("/comment-change")
    public ApiResponse<AlarmOnOff> changeComment() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(alarmSetService.commentAlarmChange(userId));
    }

    /**
     * 24.07.24 작성자 : 류기현
     * 이벤트 알람 켜기/끄기
     */
    @PatchMapping("/event-change")
    public ApiResponse<AlarmOnOff> changeEvent() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(alarmSetService.eventAlarmChange(userId));
    }

    /**
     * 24.07.24 작성자 : 류기현
     * 인기 글 알람 켜기/끄기
     */
    @PatchMapping("/hot-change")
    public ApiResponse<AlarmOnOff> changeHot() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(alarmSetService.hotAlarmChange(userId));
    }

    /**
     * 24.07.24 작성자 : 류기현
     * 좋아요 알람 켜기/끄기
     */
    @PatchMapping("/like-change")
    public ApiResponse<AlarmOnOff> changeLike() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(alarmSetService.likeAlarmChange(userId));
    }

    /**
     * 24.07.24 작성자 : 류기현
     * 공지 알람 켜기/끄기
     */
    @PatchMapping("/notice-change")
    public ApiResponse<AlarmOnOff> changeNotice() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(alarmSetService.noticeAlarmChange(userId));
    }
}
