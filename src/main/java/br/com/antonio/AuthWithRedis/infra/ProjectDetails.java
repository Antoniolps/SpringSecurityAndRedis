package br.com.antonio.AuthWithRedis.infra;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ProjectDetails {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${jwt.secret}")
    private String secret;
}
