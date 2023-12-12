package com.example.BankProject.redisCompare.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig2 {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory2(){
        //library중에서 Lettuce 라이브러리를 사용할 거임
        //application.properties에 있는 내용을 통해 접근
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    @Primary // spring boot에서 제공하는 RedisAutoConfiguration보다 이거를 우선적으로 쓰겠다.
    public RedisTemplate<String, Object> redisTemplate2() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory2());
        redisTemplate.setKeySerializer(new StringRedisSerializer()); //직렬화
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
