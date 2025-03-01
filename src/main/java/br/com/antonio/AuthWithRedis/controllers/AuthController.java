package br.com.antonio.AuthWithRedis.controllers;

import br.com.antonio.AuthWithRedis.models.Dtos.*;
import br.com.antonio.AuthWithRedis.models.User;
import br.com.antonio.AuthWithRedis.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserDto createUserDto) {
        User user = userService.register(createUserDto.email(), createUserDto.name(), createUserDto.password(), createUserDto.role());

        if(user == null){
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        return ResponseEntity.ok(new ReturnedUserDto(user.getEmail(), user.getName()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        if(!auth.isAuthenticated()){
            return ResponseEntity.badRequest().body("Email ou senha inválidos");
        }

        authService.login(loginDto.email());

        return ResponseEntity.ok("Código de verificação enviado");
    }

    @PostMapping("/2af")
    public ResponseEntity<?> verify(@RequestBody TwoFactorsDto twoFactorsDto) {
        String token = authService.verify(twoFactorsDto.email(), twoFactorsDto.code());

        if (token == null) {
            return ResponseEntity.badRequest().body("Código inválido");
        }

        return ResponseEntity.ok(new LoginResponseDto(token));
    }


}
