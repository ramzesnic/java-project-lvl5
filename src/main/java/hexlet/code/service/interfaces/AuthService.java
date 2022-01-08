package hexlet.code.service.interfaces;

import java.util.Optional;

import hexlet.code.entity.User;

public interface AuthService {
    String login(String email, String password);

    Optional<User> findByToken(String token);
}
