package umc6.tom.alarm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class AlarmRequestDto {

    @Getter
    public static class ReadAlarmDto {

        List<Long> alarmIdList;
    }
}
