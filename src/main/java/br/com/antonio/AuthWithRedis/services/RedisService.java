package br.com.antonio.AuthWithRedis.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public void saveToRedis(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getFromRedis(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

}
