package umc6.tom.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import java.security.Key;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Key, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Key, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new GenericToStringSerializer<>(Key.class));
        template.setValueSerializer(new GenericToStringSerializer<>(String.class));
        return template;
    }
}
