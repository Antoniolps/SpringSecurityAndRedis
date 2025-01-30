package br.com.antonio.AuthWithRedis.controllers;

import br.com.antonio.AuthWithRedis.models.Dtos.ShortenUrlDto;
import br.com.antonio.AuthWithRedis.models.Dtos.ShortenUrlResponseDto;
import br.com.antonio.AuthWithRedis.services.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url-shortener")
@RequiredArgsConstructor
public class UrlShortenerController {

         private final UrlShortenerService urlShortenerService;

         @PostMapping
         public ResponseEntity<?> shortenUrl(@RequestBody ShortenUrlDto shortenUrlDto) {
             String shortUrl = urlShortenerService.shortenUrl(shortenUrlDto.longUrl());

             return ResponseEntity.ok(new ShortenUrlResponseDto(shortUrl));
         }

         @GetMapping("/{shortUrl}")
         public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
             String longUrl = urlShortenerService.getLongUrl(shortUrl);

             if(longUrl == null){
                 return ResponseEntity.notFound().build();
             }

             return ResponseEntity.status(HttpStatus.FOUND)
                     .header(HttpHeaders.LOCATION, longUrl)
                     .build();
         }
}
