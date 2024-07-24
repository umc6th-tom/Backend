package umc6.tom.alarm.converter;

import umc6.tom.alarm.model.AlarmSet;
import umc6.tom.alarm.model.enums.AlarmOnOff;
import umc6.tom.user.model.User;

public class AlarmSetConverter {
    
    public static AlarmSet convertAlarmSetToAlarmSet(User user) {
        
        return AlarmSet.builder()
                .user(user)
                .pinSet(AlarmOnOff.ON)
                .commentSet(AlarmOnOff.ON)
                .eventSet(AlarmOnOff.ON)
                .hotSet(AlarmOnOff.ON)
                .likeSet(AlarmOnOff.ON)
                .noticeSet(AlarmOnOff.ON)
                .build();
    }
}
