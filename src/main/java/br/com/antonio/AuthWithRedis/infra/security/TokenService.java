package br.com.antonio.AuthWithRedis.infra.security;

import br.com.antonio.AuthWithRedis.infra.ProjectDetails;
import br.com.antonio.AuthWithRedis.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class TokenService {

    private final ProjectDetails projectDetails;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(projectDetails.getSecret());
            String token = JWT.create()
                    .withIssuer("AuthWithRedis")
                    .withSubject(user.getEmail())
                    .withExpiresAt(getExpiration())
                    .sign(algorithm);
            return token;
        }
        catch (JWTCreationException e){
            throw new RuntimeException("Erro ao criar token");
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(projectDetails.getSecret());
            return JWT.require(algorithm)
                    .withIssuer("AuthWithRedis")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (JWTVerificationException e){
            return "";
        }
    }

    private Instant getExpiration(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
