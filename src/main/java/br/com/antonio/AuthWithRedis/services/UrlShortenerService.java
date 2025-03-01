package br.com.antonio.AuthWithRedis.services;

import br.com.antonio.AuthWithRedis.infra.ProjectDetails;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final RedisService redisService;
    private final ProjectDetails projectDetails;

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

        return projectDetails.getApiUrl() + "api/url-shortener/" + uuid;
    }
}
