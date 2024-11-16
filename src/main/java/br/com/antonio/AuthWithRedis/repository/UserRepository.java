package br.com.antonio.AuthWithRedis.repository;

import br.com.antonio.AuthWithRedis.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByEmail(String email);

    Optional<User> findOptionalByEmail(String email);

    Boolean existsByEmail(String email);
}
