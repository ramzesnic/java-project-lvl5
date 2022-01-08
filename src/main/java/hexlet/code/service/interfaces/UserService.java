package hexlet.code.service.interfaces;

import java.util.List;

import hexlet.code.dto.UserDto;
import hexlet.code.dto.UserResponseDto;

public interface UserService {
    UserResponseDto getUser(long id);

    List<UserResponseDto> getUsers();

    UserResponseDto createUser(UserDto userDto);

    UserResponseDto updateUser(long id, UserDto userDto);

    void deleteUser(long id);
}
