package umc6.tom.firebase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import umc6.tom.firebase.converter.FCMConverter;
import umc6.tom.firebase.dto.FCMResponseDto;
import umc6.tom.util.RedisUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class FcmTokenService {
    private final RedisUtil redisUtil;

    private final RedisTemplate<String, Object> redisTemplate;
    public final int FCM_TOKEN_VALID_TIME_IN_REDIS = 60 * 60 * 24 * 60; //60일

    private static final String FCM_TOKEN_PREFIX = "fcm_token:user_id_";

    //ex) redis get 'fcm_token:user_id_1:"토큰값"', value:"토큰값"
    public void saveFcmToken(Long userId, String fcmToken) {
        String key = FCM_TOKEN_PREFIX + userId + ":" + fcmToken;
        redisUtil.setDataAndExpire(key, fcmToken, FCM_TOKEN_VALID_TIME_IN_REDIS);
    }

    public FCMResponseDto.fCMTokenAllListDto getAllFcmToken(Long userId) {
        String pattern = FCM_TOKEN_PREFIX + userId + ":*";
        Set<String> keys = redisUtil.getKeys(pattern);
        Set<String> values = new HashSet<>();
        for (String key : keys)
            values.add(redisUtil.getValue(key).replace("\"", ""));

        return FCMConverter.toFCMTokenAllList(userId, values);
    }

    public void deleteFcmToken(Long userId, String fcmToken) {
        String key = FCM_TOKEN_PREFIX + userId + ":" + fcmToken;
        redisUtil.deleteData(key);
    }

    public void deleteAllFcmToken(Long userId) {
        String keyPattern = FCM_TOKEN_PREFIX + userId + ":*";
        Set<String> keys = redisUtil.getKeys(keyPattern);
        if (keys != null && !keys.isEmpty()) {
            redisUtil.deleteAllData(keys);
        }
    }
    
    //댓글 등 userId 패턴해서 해당 유저토큰으로 다 보내면 될 듯

}
