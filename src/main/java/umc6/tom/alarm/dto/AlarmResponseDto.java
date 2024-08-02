package umc6.tom.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class AlarmResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class writtenBoardViewDto {
        Long BoardId;
        String category;
        String title;
        String content;
        String alarmDate; // 24.08.02
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class writtenBoardViewListDto{
        List<writtenBoardViewDto> alarmList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateReadAlarmDto{
        List<Long> alarmIdList;
    }
}
