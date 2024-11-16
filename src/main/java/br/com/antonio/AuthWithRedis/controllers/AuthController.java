package br.com.antonio.AuthWithRedis.controllers;

import br.com.antonio.AuthWithRedis.models.Dtos.*;
import br.com.antonio.AuthWithRedis.models.User;
import br.com.antonio.AuthWithRedis.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

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
        boolean isSended = authService.login(loginDto.email(), loginDto.password());

        if(!isSended){
            return ResponseEntity.badRequest().body("Email ou senha inválidos");
        }

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
