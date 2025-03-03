package br.com.antonio.AuthWithRedis.services;

import br.com.antonio.AuthWithRedis.models.Dtos.CreateUserDto;
import br.com.antonio.AuthWithRedis.models.User;
import br.com.antonio.AuthWithRedis.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwdEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("Should register user successfully")
    void registerCase1() {
        String email = "antonio@test.com.br";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        when(passwdEncoder.encode("123456")).thenReturn("123456");

        userService.register(email, "Antonio Lopes", "123456", "user");

        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw Exception when email already exists")
    void registerCase2() {
        String email = "antonio@test.com.br";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            userService.register(email, "Antonio Lopes", "123456", "user");
        });

        Assertions.assertEquals("Email já cadastrado", thrown.getMessage());
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void findByEmailCase1() {
        String email = "antonio@test.com.br";
        CreateUserDto data = new CreateUserDto(email, "Antonio Lopes", "123456", "user");
        User expectedUser = new User(data);
        Optional<User> user = Optional.of(expectedUser);

        when(userRepository.findOptionalByEmail(email)).thenReturn(user);

        User result = userService.findByEmail(email);

        verify(userRepository, times(1)).findOptionalByEmail(email);
        assertNotNull(result);
        assertEquals(expectedUser, result);
        assertEquals(email, result.getEmail());
        assertEquals("Antonio Lopes", result.getName());
    }

    @Test
    @DisplayName("Should throw Exception when user not found by email")
    void findByEmailCase2() {
        String email = "antonio@test.com.br";

        when(userRepository.findOptionalByEmail(email)).thenReturn(Optional.empty());

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            userService.findByEmail(email);
        });

        Assertions.assertEquals("Usuário não encontrado", thrown.getMessage());
        verify(userRepository, times(1)).findOptionalByEmail(email);
    }

    @Test
    @DisplayName("Should load user by email successfully")
    void loadUserByUsernameCase1() {
        String email = "antonio@test.com.br";
        CreateUserDto data = new CreateUserDto(email, "Antonio Lopes", "123456", "user");
        User expectedUser = new User(data);

        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        UserDetails result = userService.loadUserByUsername(email);

        verify(userRepository, times(1)).findByEmail(email);
        assertNotNull(result);
        assertEquals(email, result.getUsername());
    }

    @Test
    @DisplayName("Should throw Exception when user not found by email")
    void loadUserByUsernameCase2() {
        String email = "antonio@test.com.br";

        when(userRepository.findByEmail(email)).thenReturn(null);

        UserDetails result = userService.loadUserByUsername(email);

        verify(userRepository, times(1)).findByEmail(email);
        assertNull(result);
    }
}