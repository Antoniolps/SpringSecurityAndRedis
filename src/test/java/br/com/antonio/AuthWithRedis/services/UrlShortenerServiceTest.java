package br.com.antonio.AuthWithRedis.services;

import br.com.antonio.AuthWithRedis.infra.ProjectDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private RedisService redisService;

    @Mock
    private ProjectDetails projectDetails;

    @InjectMocks
    private UrlShortenerService urlShortenerService;


    @Test
    @DisplayName("Should shorten url successfully")
    void shortenUrlTest() {
        String longUrl = "https://www.example.com/very/long/url/that/needs/shortening";
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String apiUrl = "http://localhost:8080";
        String expectedShortUrl = apiUrl + "/api/url-shortener/" + uuid;

        UUID mockUUID = UUID.fromString(uuid);
        try (MockedStatic<UUID> mockedUUIDClass = mockStatic(UUID.class)) {
            mockedUUIDClass.when(UUID::randomUUID).thenReturn(mockUUID);

            when(projectDetails.getApiUrl()).thenReturn(apiUrl);

            String result = urlShortenerService.shortenUrl(longUrl);

            assertEquals(expectedShortUrl, result);
            verify(redisService, times(1)).saveToRedis(uuid, longUrl);
        }
    }

    @Test
    @DisplayName("Should get long url successfully")
    void getLongUrlCase1() {
        String longUrl = "https://www.example.com/very/long/url/that/needs/shortening";
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String shortUrl = "http://localhost:8080/api/url-shortener/" + uuid;

        when(redisService.getFromRedis(uuid)).thenReturn(longUrl);

        String result = urlShortenerService.getLongUrl(shortUrl);

        assertEquals(longUrl, result);
        verify(redisService, times(1)).getFromRedis(uuid);
    }

    @Test
    @DisplayName("Should throw exception when long url not found")
    void getLongUrlCase2() {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String shortUrl = "http://localhost:8080/api/url-shortener/" + uuid;

        when(redisService.getFromRedis(uuid)).thenReturn(null);

        Exception thrown = assertThrows(RuntimeException.class, () -> {
            urlShortenerService.getLongUrl(shortUrl);
        });

        assertEquals("URL not found", thrown.getMessage());
    }

}