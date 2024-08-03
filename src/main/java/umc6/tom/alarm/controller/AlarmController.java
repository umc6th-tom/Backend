package umc6.tom.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc6.tom.alarm.dto.AlarmRequestDto;
import umc6.tom.alarm.dto.AlarmResponseDto;
import umc6.tom.alarm.service.AlaramService;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.security.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {

    private final AlaramService alaramService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/list/board/{is_read}")
    public ApiResponse<AlarmResponseDto.writtenBoardViewListDto> getWrittenBoardAlarm(@RequestParam(name = "page") Integer page,
                                                                                   @PathVariable(name = "is_read") String isRead){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(alaramService.getBoardAlarmList(userId, isRead, page));
    }
    @GetMapping("/list/comment/{is_read}")
    public ApiResponse<AlarmResponseDto.writtenBoardViewListDto> getCommentedAlarm(@RequestParam(name = "page") Integer page,
                                                                                      @PathVariable(name = "is_read") String isRead){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(alaramService.getCommentedList(userId, isRead, page));
    }
    @GetMapping("/list/like/{is_read}")
    public ApiResponse<AlarmResponseDto.writtenBoardViewListDto> getLikedAlarm(@RequestParam(name = "page") Integer page,
                                                                                      @PathVariable(name = "is_read") String isRead){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(alaramService.getLikedList(userId, isRead, page));
    }
    @GetMapping("/list/all/{is_read}")
    public ApiResponse<AlarmResponseDto.writtenBoardViewListDto> getAllAlarm(@RequestParam(name = "page") Integer page,
                                                                                      @PathVariable(name = "is_read") String isRead){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(alaramService.getAllAlarmList(userId, isRead, page));
    }

    @PatchMapping("/list/read")
    public ApiResponse<AlarmResponseDto.updateReadAlarmDto> updateAlarmRead(@RequestBody AlarmRequestDto.ReadAlarmDto request){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(alaramService.updateReadAlarm(userId, request));
    }
}
