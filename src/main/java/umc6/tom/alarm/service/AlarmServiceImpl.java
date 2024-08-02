package umc6.tom.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc6.tom.alarm.converter.AlarmConverter;
import umc6.tom.alarm.dto.AlarmRequestDto;
import umc6.tom.alarm.dto.AlarmResponseDto;
import umc6.tom.alarm.model.Alarm;
import umc6.tom.alarm.model.enums.Field;
import umc6.tom.alarm.model.enums.IsRead;
import umc6.tom.alarm.repository.AlarmRepository;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.AlarmHandler;
import umc6.tom.apiPayload.exception.handler.BoardHandler;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlaramService{

    private final AlarmRepository alarmRepository;

    @Override
    public AlarmResponseDto.writtenBoardViewListDto getBoardAlarmList(Long userId, String isRead, Integer page) {
        IsRead isReadVal;
        try {
            isReadVal= IsRead.valueOf(isRead.toUpperCase());
        }catch (Exception e){
            throw new AlarmHandler(ErrorStatus.ALARM_READ_NOT_FOUND);
        }
        Page<Alarm> alarmPage = alarmRepository.findAllByUserIdAndIsReadAndCategoryOrderByCreatedAtDesc
                (userId, isReadVal, Field.WrittenBoard, PageRequest.of(page,12));

        return AlarmConverter.toBoardListViewListDTO(alarmPage);
    }

    @Override
    public AlarmResponseDto.writtenBoardViewListDto getCommentedList(Long userId, String isRead, Integer page) {
        IsRead isReadVal;
        try {
            isReadVal= IsRead.valueOf(isRead.toUpperCase());
        }catch (Exception e){
            throw new AlarmHandler(ErrorStatus.ALARM_READ_NOT_FOUND);
        }
        Page<Alarm> alarmPage = alarmRepository.findAllByUserIdAndIsReadAndCategoryOrderByCreatedAtDesc
                (userId, isReadVal, Field.commented, PageRequest.of(page,12));
        return AlarmConverter.toBoardListViewListDTO(alarmPage);
    }

    @Override
    public AlarmResponseDto.writtenBoardViewListDto getLikedList(Long userId, String isRead, Integer page) {
        IsRead isReadVal;
        try {
            isReadVal= IsRead.valueOf(isRead.toUpperCase());
        }catch (Exception e){
            throw new AlarmHandler(ErrorStatus.ALARM_READ_NOT_FOUND);
        }
        Page<Alarm> alarmPage = alarmRepository.findAllByUserIdAndIsReadAndCategoryOrderByCreatedAtDesc
                (userId, isReadVal, Field.liked, PageRequest.of(page,12));
        return AlarmConverter.toBoardListViewListDTO(alarmPage);
    }

    @Override
    public AlarmResponseDto.writtenBoardViewListDto getAllAlarmList(Long userId, String isRead, Integer page) {
        IsRead isReadVal;
        try {
            isReadVal= IsRead.valueOf(isRead.toUpperCase());
        }catch (Exception e){
            throw new AlarmHandler(ErrorStatus.ALARM_READ_NOT_FOUND);
        }
        Page<Alarm> alarmPage = alarmRepository.findAllByUserIdAndIsReadOrderByCreatedAtDesc
                (userId, isReadVal, PageRequest.of(page, 12));
        return AlarmConverter.toBoardListViewListDTO(alarmPage);
    }

    @Override
    public AlarmResponseDto.updateReadAlarmDto updateReadAlarm(Long userId, AlarmRequestDto.ReadAlarmDto request) {
        for (Long alarmId : request.getAlarmIdList()) {
            Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(()
                    -> new AlarmHandler(ErrorStatus.ALARM_NOT_FOUND));
            if (!alarm.getUser().getId().equals(userId))
                throw new AlarmHandler(ErrorStatus.ALARM_NOT_MATCH);
            alarm.setIsRead(IsRead.YES);
            alarmRepository.save(alarm);
        }
        return AlarmConverter.toReadAlarmDto(request.getAlarmIdList());
    }
}
