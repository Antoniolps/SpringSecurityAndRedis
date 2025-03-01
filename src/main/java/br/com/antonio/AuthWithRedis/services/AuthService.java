package br.com.antonio.AuthWithRedis.services;

import br.com.antonio.AuthWithRedis.models.User;
import br.com.antonio.AuthWithRedis.infra.security.TokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final RedisService redisService;
    private final TokenService tokenService;

    public void login(String email){
        String code = String.format("%06d", new Random().nextInt(999999));
        redisService.saveToRedis(email, code);

        // TODO: Serviço de envio de email
        System.out.println("Código de verificação: " + code);// Simulando envio de email
    }

    public String verify(String email, String code){
        String storedCode = redisService.getFromRedis(email);

        if(storedCode == null || !storedCode.equals(code)){
            return null;
        }

        redisService.delete(email);
        User user = userService.findByEmail(email);
        String accessToken = tokenService.generateToken(user);


        return accessToken;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.loadUserByUsername(username);
    }
}
