package umc6.tom.firebase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.firebase.converter.FCMConverter;
import umc6.tom.firebase.dto.FCMRequestDto;
import umc6.tom.firebase.dto.FCMResponseDto;
import umc6.tom.firebase.service.FcmTokenService;
import umc6.tom.firebase.service.FirebaseService;
import umc6.tom.security.JwtTokenProvider;

import java.io.IOException;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FCMController {
    private final FirebaseService firebaseService;
    private final FcmTokenService fcmTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 24.07.29 작성자 : 박재락
     * 해당 토큰 디바이스에 메세지 보내기
     */
    @PostMapping("/alarm-message")
    public ApiResponse<FCMResponseDto.sendMessageDto> pushMessage(@RequestBody FCMRequestDto.AlarmPushDto request) throws IOException {
        firebaseService.sendMessageTo(request.getTargetToken(), request.getTitle(), request.getBody());

        return ApiResponse.onSuccess(FCMConverter.toSendMessage(request.getTargetToken(), request.getTitle(), request.getBody()));
    }

    /**
     * 24.07.30 작성자 : 박재락
     * 서버에 토큰 저장
     */
    //서버에 토큰 등록 userId, 토큰
    @PostMapping("/saveFcmToken")
    public ApiResponse<FCMResponseDto.saveTokenDto> saveToken(@RequestBody FCMRequestDto.fcmTokenDto fcmToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        fcmTokenService.saveFcmToken(userId, fcmToken.getTargetToken());
        return ApiResponse.onSuccess(FCMConverter.toSaveToken(userId, fcmToken.getTargetToken()));
    }

    /**
     * 24.07.30 작성자 : 박재락
     * 유저의 토큰 불러오기 (api가 필요한가?)
     */
    @GetMapping("/getFcmToken")
    public ApiResponse<FCMResponseDto.fCMTokenAllListDto> getToken() {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(fcmTokenService.getAllFcmToken(userId));
    }
    /**
     * 24.07.30 작성자 : 박재락
     * 한개의 토큰 삭제 (한 디바이스의 로그 아웃) (api가 필요한가?)
     */
    @DeleteMapping("/deleteFcmToken")
    public ApiResponse<FCMResponseDto.deleteTokenDto> deleteToken(@RequestBody FCMRequestDto.fcmTokenDto fcmToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        fcmTokenService.deleteFcmToken(userId, fcmToken.getTargetToken());
        return ApiResponse.onSuccess(FCMConverter.toDeleteToken(userId, fcmToken.getTargetToken()));
    }
    /**
     * 24.07.30 작성자 : 박재락
     * 유저의 모든 토큰 삭제 (회원 탈퇴 등)
     */
    @DeleteMapping("/deleteAllFcmToken")
    public ApiResponse<FCMResponseDto.fCMTokenAllListDto> deleteAllToken() {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        fcmTokenService.deleteAllFcmToken(userId);
        return ApiResponse.onSuccess(fcmTokenService.getAllFcmToken(userId));
    }
}
