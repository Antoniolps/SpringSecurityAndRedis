package br.com.antonio.AuthWithRedis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    // Configura a conexão com o Redis
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        // Aqui você pode configurar o Redis com host, port, senha, etc.
        return new LettuceConnectionFactory("localhost", 6379);
    }


    // Configura o RedisTemplate, que será usado para interagir com o Redis
    @Bean
    public RedisTemplate<String, String> redisTemplate(){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    // Configura o StringRedisTemplate, útil quando você vai trabalhar apenas com Strings
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
