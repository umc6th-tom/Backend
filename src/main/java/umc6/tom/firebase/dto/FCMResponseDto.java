package umc6.tom.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

public class FCMResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class fCMTokenAllListDto {
        Long userId;
        Set<String> fCmTokens;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class sendMessageDto {
        String token;
        String title;
        String body;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class saveTokenDto {
        Long userId;
        String fCMToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class deleteTokenDto {
        Long userId;
        String fCMToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class deleteAllTokensDto {
        Long userId;
        Set<String> fCMTokens;
    }

}
