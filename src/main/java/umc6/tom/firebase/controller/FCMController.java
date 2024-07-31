package umc6.tom.firebase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc6.tom.firebase.dto.FCMRequestDto;
import umc6.tom.firebase.service.FcmTokenService;
import umc6.tom.firebase.service.FirebaseService;
import umc6.tom.security.JwtTokenProvider;

import java.io.IOException;



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
    public ResponseEntity pushMessage(@RequestBody FCMRequestDto.AlarmPushDto request) throws IOException {
        firebaseService.sendMessageTo(request.getTargetToken(), request.getTitle(), request.getBody());
        return ResponseEntity.ok().build();
    }

    /**
     * 24.07.30 작성자 : 박재락
     * 서버에 토큰 저장
     */
    //서버에 토큰 등록 userId, 토큰
    @PostMapping("/saveFcmToken/{user_id}")
    public ResponseEntity saveToken(@RequestBody String fcmToken) throws IOException {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        fcmTokenService.saveFcmToken(userId, fcmToken);
        return ResponseEntity.ok().build();
    }

    /**
     * 24.07.30 작성자 : 박재락
     * 유저의 토큰 불러오기 (api가 필요한가?)
     */
    @GetMapping("/getFcmToken/{user_id}")
    public ResponseEntity getToken() throws IOException {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        fcmTokenService.getAllFcmToken(userId);
        return ResponseEntity.ok().build();
    }
    /**
     * 24.07.30 작성자 : 박재락
     * 한개의 토큰 삭제 (한 디바이스의 로그 아웃) (api가 필요한가?)
     */
    @DeleteMapping("/deleteFcmToken/{user_id}")
    public ResponseEntity deleteToken(@RequestBody String fcmToken) throws IOException {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        fcmTokenService.deleteFcmToken(userId, fcmToken);
        return ResponseEntity.ok().build();
    }
    /**
     * 24.07.30 작성자 : 박재락
     * 유저의 모든 토큰 삭제 (회원 탈퇴 등)
     */
    @DeleteMapping("/deleteAllFcmToken/{user_id}")
    public ResponseEntity deleteAllToken(@RequestParam(name = "user_id") Long userId) throws IOException {
        //Long userId = jwtTokenProvider.getUserIdFromToken();
        fcmTokenService.deleteAllFcmToken(userId);
        return ResponseEntity.ok().build();
    }

    
    //로그 아웃, 일정기간 지난 토큰 삭제 createAt으로 매일 자정에 삭제? api 요청시 토큰이 만료시 유저 토큰 삭제?
    
    //2달안에 앱 접속시 토큰 기간 연장 refresh


}
