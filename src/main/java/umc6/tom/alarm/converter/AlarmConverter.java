package umc6.tom.alarm.converter;

import org.springframework.data.domain.Page;
import umc6.tom.alarm.dto.AlarmResponseDto;
import umc6.tom.alarm.model.Alarm;
import umc6.tom.alarm.model.enums.Field;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.board.model.Board;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmConverter {

    public static Alarm toAlarm(Board board, Field category, String alarm){
        return Alarm.builder()
                .category(category)
                .user(board.getUser())
                .board(board)
                .alarm(alarm)
                .build();
    }

    public static AlarmResponseDto.writtenBoardViewDto toWrittenBoardView(Alarm alarm){
        return AlarmResponseDto.writtenBoardViewDto.builder()
                .BoardId(alarm.getBoard().getId())
                .category(alarm.getCategory().getKor())
                .title(alarm.getAlarm())
                .content(alarm.getBoard().getTitle())
                .alarmDate(new DateCalc().formatDate2(alarm.getCreatedAt()))
                .build();
    }
    public static AlarmResponseDto.writtenBoardViewListDto toBoardListViewListDTO(Page<Alarm> alarmList){
        List<AlarmResponseDto.writtenBoardViewDto> alarmListViewDtoList = alarmList.stream()
                .map(AlarmConverter::toWrittenBoardView).collect(Collectors.toList());

        return AlarmResponseDto.writtenBoardViewListDto.builder()
                .isLast(alarmList.isLast())
                .isFirst(alarmList.isFirst())
                .totalPage(alarmList.getTotalPages())
                .totalElements(alarmList.getTotalElements())
                .listSize(alarmListViewDtoList.size())
                .alarmList(alarmListViewDtoList)
                .build();
    }

    public static AlarmResponseDto.updateReadAlarmDto toReadAlarmDto(List<Long> alarmIdList){
        return AlarmResponseDto.updateReadAlarmDto.builder()
                .alarmIdList(alarmIdList)
                .build();
    }
}
