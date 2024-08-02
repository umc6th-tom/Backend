package umc6.tom.firebase.converter;

import umc6.tom.firebase.dto.FCMResponseDto;

import java.util.Set;

public class FCMConverter {

    public static FCMResponseDto.fCMTokenAllListDto toFCMTokenAllList(Long userId, Set<String> tokens){
        return FCMResponseDto.fCMTokenAllListDto.builder()
                .userId(userId)
                .fCmTokens(tokens)
                .build();

    }

    public static FCMResponseDto.sendMessageDto toSendMessage(String token,String title,String body){
        return FCMResponseDto.sendMessageDto.builder()
                .token(token)
                .title(title)
                .body(body)
                .build();

    }

    public static FCMResponseDto.saveTokenDto toSaveToken(Long userId, String token){
        return FCMResponseDto.saveTokenDto.builder()
                .userId(userId)
                .fCMToken(token)
                .build();

    }

    public static FCMResponseDto.deleteTokenDto toDeleteToken(Long userId, String token){
        return FCMResponseDto.deleteTokenDto.builder()
                .userId(userId)
                .fCMToken(token)
                .build();

    }

    public static FCMResponseDto.deleteAllTokensDto toDeleteAllTokens(Long userId, Set<String> tokens){
        return FCMResponseDto.deleteAllTokensDto.builder()
                .userId(userId)
                .fCMTokens(tokens)
                .build();

    }

}
