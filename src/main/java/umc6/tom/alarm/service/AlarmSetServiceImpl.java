package umc6.tom.alarm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.alarm.model.AlarmSet;
import umc6.tom.alarm.model.enums.AlarmOnOff;
import umc6.tom.alarm.repository.AlarmSetRepository;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.AlarmSetHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class AlarmSetServiceImpl implements AlarmSetService {
    private final AlarmSetRepository alarmSetRepository;
    private final UserRepository userRepository;

    @Override
    public void alarmSetAllChange(Long userId) {

        AlarmSet alarmSet = getAlarmSetFromUser(userId);

        alarmSet.setPinSet(AlarmOnOff.ON);
        alarmSet.setCommentSet(AlarmOnOff.ON);
        alarmSet.setEventSet(AlarmOnOff.ON);
        alarmSet.setHotSet(AlarmOnOff.ON);
        alarmSet.setLikeSet(AlarmOnOff.ON);
        alarmSet.setNoticeSet(AlarmOnOff.ON);
    }

    @Override
    public AlarmOnOff pinAlarmChange(Long userId) {

        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        alarmSet.setPinSet(AlarmOnOff.OFF);
        return alarmSet.getPinSet();
    }

    @Override
    public AlarmOnOff commentAlarmChange(Long userId) {

        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        alarmSet.setCommentSet(AlarmOnOff.ON);
        return alarmSet.getCommentSet();
    }

    @Override
    public AlarmOnOff eventAlarmChange(Long userId) {

        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        alarmSet.setEventSet(AlarmOnOff.ON);
        return alarmSet.getEventSet();
    }

    @Override
    public AlarmOnOff hotAlarmChange(Long userId) {

        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        alarmSet.setHotSet(AlarmOnOff.ON);
        return alarmSet.getHotSet();
    }

    @Override
    public AlarmOnOff likeAlarmChange(Long userId) {

        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        alarmSet.setLikeSet(AlarmOnOff.ON);
        return alarmSet.getLikeSet();
    }

    @Override
    public AlarmOnOff noticeAlarmChange(Long userId) {

        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        alarmSet.setNoticeSet(AlarmOnOff.ON);
        return alarmSet.getNoticeSet();
    }

    @Override
    public AlarmSet getAlarmSetFromUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return alarmSetRepository.findByUser(user)
                .orElseThrow(() -> new AlarmSetHandler(ErrorStatus.ALARM_SET_NOT_FOUND));
    }
}
