package hexlet.code.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.dto.UserDto;
import hexlet.code.dto.UserResponseDto;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("${base-url}" + "users")
public final class UserController {
    public static final String ID = "/{id}";
    @Autowired
    private UserService userService;

    @Operation(summary = "Get user by id")
    @GetMapping(path = ID)
    public UserResponseDto getUser(@PathVariable("id")
    long id) {
        return this.userService.getUser(id);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public List<UserResponseDto> getUsers() {
        return this.userService.getUsers();
    }

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "201")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDto createUser(@RequestBody @Valid
    UserDto userDto) {
        return this.userService.createUser(userDto);
    }

    @Operation(summary = "Update user")
    @PutMapping(path = ID)
    public UserResponseDto updateUser(@PathVariable("id")
    long id, @RequestBody @Valid
    UserDto userDto) {
        return this.userService.updateUser(id, userDto);
    }

    @Operation(summary = "Delete user")
    @DeleteMapping(path = ID)
    public void deleteUser(@PathVariable("id")
    long id) {
        this.userService.deleteUser(id);
    }
}
