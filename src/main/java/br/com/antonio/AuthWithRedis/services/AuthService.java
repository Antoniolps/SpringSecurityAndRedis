package br.com.antonio.AuthWithRedis.services;

import br.com.antonio.AuthWithRedis.models.User;
import br.com.antonio.AuthWithRedis.infra.security.TokenService;
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
public class AuthService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;


    public boolean login(String email, String password){
        var usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        var auth = authenticationManager.authenticate(usernamePassword);

        if(!auth.isAuthenticated()){
            return false;
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        redisService.saveToRedis(email, code);

        // TODO: Serviço de envio de email
        System.out.println("Código de verificação: " + code);// Simulando envio de email
        return true;
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
