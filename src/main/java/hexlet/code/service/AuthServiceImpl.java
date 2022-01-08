package hexlet.code.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.interfaces.AuthService;
import hexlet.code.service.interfaces.TokenService;

@Service
public final class AuthServiceImpl implements AuthService {
    private static final String INVALID_AUTH_DATA = "Invalid user or password";
    private static final String AUTH_FIELD = "email";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        return this.userRepository.findByEmail(email)
                .filter(user -> this.passwordEncoder.matches(password, user.getPassword()))
                .map(user -> this.tokenService.expiring(Map.of(AUTH_FIELD, email)))
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_AUTH_DATA));
    }

    @Override
    public Optional<User> findByToken(String token) {
        String email = this.tokenService.verify(token)
                .get(AUTH_FIELD)
                .toString();

        return this.userRepository.findByEmail(email);
    }
}
