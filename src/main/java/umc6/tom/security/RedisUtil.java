package umc6.tom.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<Key, String> redisTemplate;

    public String getData(Key key) {
        ValueOperations<Key, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setDataExpire(Key key, String value, long duration) {
        ValueOperations<Key, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void setData(Key key, String value) {
        ValueOperations<Key, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void deleteData(Key key) {
        redisTemplate.delete(key);
    }

    public Long getExpiration(Key key) {
        return redisTemplate.getExpire(key);
    }
}

