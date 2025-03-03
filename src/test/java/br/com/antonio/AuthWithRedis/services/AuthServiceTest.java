package br.com.antonio.AuthWithRedis.services;

import br.com.antonio.AuthWithRedis.infra.security.TokenService;
import br.com.antonio.AuthWithRedis.models.Dtos.CreateUserDto;
import br.com.antonio.AuthWithRedis.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RedisService redisService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @Captor
    private ArgumentCaptor<String> codeCaptor;

    @Test
    @DisplayName("Should create a code and Save on RedisService")
    void login() {
        String email = "antonio@test.com.br";

        authService.login(email);

        verify(redisService, times(1)).saveToRedis(eq(email), codeCaptor.capture());

        String generatedCode = codeCaptor.getValue();

        assertNotNull(generatedCode);

        assertEquals(6, generatedCode.length());

        assertTrue(generatedCode.matches("\\d{6}"));
    }

    @Test
    @DisplayName("Should return an accessToken when the code is valid")
    void verifyCase1() {
        String code = "123456";
        String email = "antonio@test.com.br";
        String expectedToken = "expected-token";
        User mockUser = new User();

        when(redisService.getFromRedis(email)).thenReturn(code);
        when(userService.findByEmail(email)).thenReturn(mockUser);
        when(tokenService.generateToken(mockUser)).thenReturn(expectedToken);

        String result = authService.verify(email, code);

        assertEquals(expectedToken, result);

        verify(redisService, times(1)).delete(email);

        verify(redisService, times(1)).getFromRedis(email);
        verify(userService, times(1)).findByEmail(email);
        verify(tokenService, times(1)).generateToken(mockUser);
    }

    @Test
    @DisplayName("Should throw an exception when the code is not valid")
    void verifyCase2() {
        String code = "123456";
        String email = "antonio@test.com.br";
        String providedCode = "654321";

        when(redisService.getFromRedis(email)).thenReturn(code);

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            authService.verify(email, providedCode);
        });

        assertEquals("Código inválido!", thrown.getMessage());

        verify(redisService, never()).delete(email);
        verify(userService, never()).findByEmail(anyString());
        verify(tokenService, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should throw an exception when the code is null")
    void verifyCase3() {
        String email = "antonio@test.com.br";
        String providedCode = "654321";

        when(redisService.getFromRedis(email)).thenReturn(null);

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            authService.verify(email, providedCode);
        });

        assertEquals("Código inválido!", thrown.getMessage());

        verify(redisService, never()).delete(email);
        verify(userService, never()).findByEmail(anyString());
        verify(tokenService, never()).generateToken(any(User.class));

    }
}