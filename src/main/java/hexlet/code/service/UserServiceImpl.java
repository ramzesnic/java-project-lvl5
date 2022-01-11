package hexlet.code.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hexlet.code.dto.UserDto;
import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.interfaces.UserService;

@Service
public final class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUser(long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return user;
    }

    @Override
    public User updateUser(long id, UserDto userDto) {
        User user = this.userRepository.findById(id)
                .orElseThrow();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return user;
    }

    @Override
    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }
}
