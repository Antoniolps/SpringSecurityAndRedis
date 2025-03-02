package br.com.antonio.AuthWithRedis.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        lenient().when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("Test saveToRedis")
    void saveToRedis() {
        String testKey = "testKey";
        String testValue = "testValue";

        redisService.saveToRedis(testKey, testValue);

        verify(valueOperations, times(1)).set(testKey, testValue);
    }

    @Test
    @DisplayName("Test getFromRedis")
    void getFromRedis() {
        String testKey = "testKey";
        String testValue = "testValue";

        when(valueOperations.get(testKey)).thenReturn(testValue);

        String result = redisService.getFromRedis(testKey);

        assertEquals(testValue, result);
        verify(valueOperations, times(1)).get(testKey);
    }

    @Test
    @DisplayName("Test delete")
    void delete() {
        String testKey = "testKey";

        redisService.delete(testKey);

        verify(stringRedisTemplate, times(1)).delete(testKey);
    }
}