package br.com.antonio.AuthWithRedis.infra.security;

import br.com.antonio.AuthWithRedis.config.ProjectDetails;
import br.com.antonio.AuthWithRedis.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private ProjectDetails projectDetails;

    @Spy
    @InjectMocks
    private TokenService tokenService;

    private User user;
    private static final String SECRET = "test-secret-key-for-jwt-generation";
    private static final String EMAIL = "antonio@test.com.br";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);

        when(projectDetails.getSecret()).thenReturn(SECRET);
    }

    @Test
    @DisplayName("Should return a valid token")
    void generateTokenCase1() {
        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        String sub = tokenService.validateToken(token);
        assertEquals(EMAIL, sub);
    }

    @Test
    @DisplayName("Validate token with valid token, should return subject")
    void validateTokenCase1() {
        String token = tokenService.generateToken(user);

        String sub = tokenService.validateToken(token);

        assertEquals(EMAIL, sub);
    }

    @Test
    @DisplayName("Validate token with invalid token, should return empty string")
    void validateTokenCase2() {
        String invalidToken = "invalid.token.string";

        String subject = tokenService.validateToken(invalidToken);

        assertTrue(subject.isEmpty());
    }

    @Test
    @DisplayName("Validate token with expired token, should return empty string")
    void validateTokenCase3() {
        Instant pastExpiration = Instant.now().minus(1, ChronoUnit.HOURS);

        doReturn(pastExpiration).when(tokenService).getExpiration();

        String expiredToken = tokenService.generateToken(user);

        reset(tokenService);
        when(projectDetails.getSecret()).thenReturn(SECRET);

        String subject = tokenService.validateToken(expiredToken);

        assertTrue(subject.isEmpty());
    }

    @Test
    @DisplayName("Should throw exception, when the secret is null")
    void generateTokenCase2() {
        when(projectDetails.getSecret()).thenReturn(null);

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            tokenService.generateToken(user);
        });

        assertEquals( "The Secret cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("Should return different token, whit different users")
    void generateTokenCase3() {
        String email = "antonio@test2.com.br";
        User anotherUser = new User();
        anotherUser.setEmail(email);

        String token1 = tokenService.generateToken(user);
        String token2 = tokenService.generateToken(anotherUser);

        assertNotEquals(token1, token2);

        assertEquals(EMAIL, tokenService.validateToken(token1));
        assertEquals(email, tokenService.validateToken(token2));
    }

}