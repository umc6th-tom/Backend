package umc6.tom.alarm.service;

import umc6.tom.alarm.dto.AlarmRequestDto;
import umc6.tom.alarm.dto.AlarmResponseDto;


public interface AlaramService {

    AlarmResponseDto.writtenBoardViewListDto getBoardAlarmList(Long userId, String isRead, Integer page);
    AlarmResponseDto.writtenBoardViewListDto getCommentedList(Long userId, String isRead, Integer page);
    AlarmResponseDto.writtenBoardViewListDto getLikedList(Long userId, String isRead, Integer page);
    AlarmResponseDto.writtenBoardViewListDto getAllAlarmList(Long userId, String isRead, Integer page);
    AlarmResponseDto.updateReadAlarmDto updateReadAlarm(Long userId, AlarmRequestDto.ReadAlarmDto request);
}
