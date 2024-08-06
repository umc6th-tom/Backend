package umc6.tom.alarm.dto;

import lombok.Getter;

import java.util.List;

public class AlarmRequestDto {

    @Getter
    public static class ReadAlarmDto {

        List<Long> alarmIdList;
    }
}
