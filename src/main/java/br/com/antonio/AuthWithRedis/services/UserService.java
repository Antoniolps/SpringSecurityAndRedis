package br.com.antonio.AuthWithRedis.services;

import br.com.antonio.AuthWithRedis.models.Enums.UserRole;
import br.com.antonio.AuthWithRedis.repository.UserRepository;
import br.com.antonio.AuthWithRedis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwdEncoder = new BCryptPasswordEncoder();

    public User register(String email, String name, String password, String role){
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwdEncoder.encode(password));
        user.setRole(UserRole.valueOf(role));
        return userRepository.save(user);

    }

    public User findByEmail(String email) throws RuntimeException{
        return userRepository.findOptionalByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UserDetails loadUserByUsername(String email) throws RuntimeException{
        return userRepository.findByEmail(email);
    }
}
