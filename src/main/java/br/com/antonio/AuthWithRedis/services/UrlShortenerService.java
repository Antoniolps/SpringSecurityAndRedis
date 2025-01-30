package br.com.antonio.AuthWithRedis.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlShortenerService {

    private final RedisService redisService;

    private final String apiUrl = "http://localhost:8080/";

    public String shortenUrl(String longUrl){
        String shortUrl = generateShortUrl(longUrl);
        redisService.saveToRedis(shortUrl, longUrl);
        return shortUrl;
    }

    public String getLongUrl(String shortUrl){
        return redisService.getFromRedis(shortUrl);
    }

    private String generateShortUrl(String longUrl){
        UUID uuid = UUID.randomUUID();

        redisService.saveToRedis(uuid.toString(), longUrl);

        return apiUrl + "api/url-shortener/" + uuid.toString();
    }
}
