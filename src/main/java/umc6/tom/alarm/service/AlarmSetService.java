package umc6.tom.alarm.service;

import umc6.tom.alarm.model.AlarmSet;
import umc6.tom.alarm.model.enums.AlarmOnOff;

public interface AlarmSetService {

    void alarmSetAllChange(Long userId);

    AlarmOnOff pinAlarmChange(Long userId);

    AlarmOnOff commentAlarmChange(Long userId);

    AlarmOnOff eventAlarmChange(Long userId);

    AlarmOnOff hotAlarmChange(Long userId);

    AlarmOnOff likeAlarmChange(Long userId);

    AlarmOnOff noticeAlarmChange(Long userId);

    AlarmSet getAlarmSetFromUser(Long userId);
}
