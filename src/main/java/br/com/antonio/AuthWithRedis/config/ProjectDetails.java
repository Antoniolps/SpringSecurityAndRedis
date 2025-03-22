package br.com.antonio.AuthWithRedis.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ProjectDetails {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${jwt.secret}")
    private String secret;
}
