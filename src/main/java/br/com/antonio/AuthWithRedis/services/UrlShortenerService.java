package br.com.antonio.AuthWithRedis.services;

import br.com.antonio.AuthWithRedis.config.ProjectDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final RedisService redisService;
    private final ProjectDetails projectDetails;

    public String shortenUrl(String longUrl){
        UUID uuid = UUID.randomUUID();

        redisService.saveToRedis(uuid.toString(), longUrl);

        return projectDetails.getApiUrl() + "/api/url-shortener/" + uuid;
    }

    public String getLongUrl(String shortUrl){
        String uuid = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);

        String originalUrl = redisService.getFromRedis(uuid);

        if(originalUrl == null){
            throw new RuntimeException("URL not found");
        }

        return originalUrl;
    }

}
