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
        if (alarmSet != null) {
            alarmSet.setPinSet(AlarmOnOff.ON);
            alarmSet.setCommentSet(AlarmOnOff.ON);
            alarmSet.setEventSet(AlarmOnOff.ON);
            alarmSet.setHotSet(AlarmOnOff.ON);
            alarmSet.setLikeSet(AlarmOnOff.ON);
            alarmSet.setNoticeSet(AlarmOnOff.ON);
        }
    }

    @Override
    public AlarmOnOff pinAlarmChange(Long userId) {
        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        if (alarmSet != null) {
            if (alarmSet.getPinSet() == AlarmOnOff.OFF) {
                alarmSet.setPinSet(AlarmOnOff.ON);
            } else {
                alarmSet.setPinSet(AlarmOnOff.OFF);
            }
            return alarmSet.getPinSet();
        }
        return null;
    }

    @Override
    public AlarmOnOff commentAlarmChange(Long userId) {
        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        if (alarmSet != null) {
            if (alarmSet.getCommentSet() == AlarmOnOff.OFF) {
                alarmSet.setCommentSet(AlarmOnOff.ON);
            } else {
                alarmSet.setCommentSet(AlarmOnOff.OFF);
            }
            return alarmSet.getCommentSet();
        }
        return null;
    }

    @Override
    public AlarmOnOff eventAlarmChange(Long userId) {
        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        if (alarmSet != null) {
            if (alarmSet.getEventSet() == AlarmOnOff.OFF) {
                alarmSet.setEventSet(AlarmOnOff.ON);
            } else {
                alarmSet.setEventSet(AlarmOnOff.OFF);
            }
            return alarmSet.getEventSet();
        }
        return null;
    }

    @Override
    public AlarmOnOff hotAlarmChange(Long userId) {
        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        if (alarmSet != null) {
            if (alarmSet.getHotSet() == AlarmOnOff.OFF) {
                alarmSet.setHotSet(AlarmOnOff.ON);
            } else {
                alarmSet.setHotSet(AlarmOnOff.OFF);
            }
            return alarmSet.getHotSet();
        }
        return null;
    }

    @Override
    public AlarmOnOff likeAlarmChange(Long userId) {
        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        if (alarmSet != null) {
            if (alarmSet.getLikeSet() == AlarmOnOff.OFF) {
                alarmSet.setLikeSet(AlarmOnOff.ON);
            } else {
                alarmSet.setLikeSet(AlarmOnOff.OFF);
            }
            return alarmSet.getLikeSet();
        }
        return null;
    }

    @Override
    public AlarmOnOff noticeAlarmChange(Long userId) {
        AlarmSet alarmSet = getAlarmSetFromUser(userId);
        if (alarmSet != null) {
            if (alarmSet.getNoticeSet() == AlarmOnOff.OFF) {
                alarmSet.setNoticeSet(AlarmOnOff.ON);
            } else {
                alarmSet.setNoticeSet(AlarmOnOff.OFF);
            }
            return alarmSet.getNoticeSet();
        }
        return null;
    }

    @Override
    public AlarmSet getAlarmSetFromUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return alarmSetRepository.findByUser(user)
                .orElseThrow(() -> new AlarmSetHandler(ErrorStatus.ALARM_SET_NOT_FOUND));
    }
}
