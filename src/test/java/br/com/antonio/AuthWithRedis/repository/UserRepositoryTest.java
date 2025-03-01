package br.com.antonio.AuthWithRedis.repository;

import br.com.antonio.AuthWithRedis.models.Dtos.CreateUserDto;
import br.com.antonio.AuthWithRedis.models.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get user details successfully by email")
    void findByEmailCase1() {
        String email = "antonio@test.com.br";
        CreateUserDto data = new CreateUserDto("antonio@test.com.br", "Antonio Lopes", "123456", "user");
        createUser(data);

        UserDetails result = userRepository.findByEmail(email);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should not get user details by email when user not exists")
    void findByEmailCase2() {
        String email = "antonio@test.com.br";

        UserDetails result = userRepository.findByEmail(email);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should get user by email successfully")
    void findOptionalByEmailCase1() {
        String email = "antonio@test.com.br";
        CreateUserDto data = new CreateUserDto("antonio@test.com.br", "Antonio Lopes", "123456", "user");
        createUser(data);

        Optional<User> result = userRepository.findOptionalByEmail(email);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user by email when user not exists")
    void findOptionalByEmailCase2() {
        String email = "antonio@test.com.br";

        Optional<User> result = userRepository.findOptionalByEmail(email);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should return true when user exists by email")
    void existsByEmailCase1() {
        String email = "antonio@test.com.br";
        CreateUserDto data = new CreateUserDto("antonio@test.com.br", "Antonio Lopes", "123456", "user");
        createUser(data);

        Boolean result = userRepository.existsByEmail(email);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when user not exists by email")
    void existsByEmailCase2() {
        String email = "antonio@test.com.br";

        Boolean result = userRepository.existsByEmail(email);

        assertThat(result).isFalse();
    }


    private User createUser(CreateUserDto data){
        User user = new User(data);
        this.entityManager.persist(user);
        return user;
    }
}