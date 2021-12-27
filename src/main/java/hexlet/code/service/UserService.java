package hexlet.code.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hexlet.code.dto.UserDto;
import hexlet.code.dto.UserResponseDto;
import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;

@Service
public final class UserService {
    @Autowired
    private UserRepository userRepository;
    //private PasswordEncoder passwordEncoder;

    public UserResponseDto getUser(long id) {
        UserResponseDto userDto = this.userRepository.findById(id)
                .map(UserResponseDto::new)
                .orElseThrow();
        return userDto;
    }

    public List<UserResponseDto> getUsers() {
        List<UserResponseDto> usersDto = this.userRepository.findAll()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return usersDto;
    }

    public UserResponseDto createUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        //user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPassword(userDto.getPassword());

        return new UserResponseDto(this.userRepository.save(user));
    }

    public UserResponseDto updateUser(long id, UserDto userDto) {
        User user = this.userRepository.findById(id)
                .orElseThrow();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        //user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPassword(userDto.getPassword());

        return new UserResponseDto(this.userRepository.save(user));
    }

    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }
}
