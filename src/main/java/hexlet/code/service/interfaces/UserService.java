package hexlet.code.service.interfaces;

import java.util.List;
import java.util.Optional;

import hexlet.code.dto.UserDto;
import hexlet.code.entity.User;

public interface UserService {
    Optional<User> getUser(long id);

    List<User> getUsers();

    User createUser(UserDto userDto);

    User updateUser(long id, UserDto userDto);

    void deleteUser(long id);
}
