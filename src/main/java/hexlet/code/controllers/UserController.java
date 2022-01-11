package hexlet.code.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
import hexlet.code.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("${base-url}" + "users")
public class UserController {
    private static final String ID = "/{id}";
    public static final String USER_CONTROLLER_PATH = "users";
    private static final String ONLY_OWNER_BY_ID = "#id == principal.getUserId()";
    @Autowired
    private UserService userService;

    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserResponseDto getUser(@PathVariable("id")
    long id) {
        return this.userService.getUser(id)
                .map(UserResponseDto::new)
                .orElseThrow();
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public List<UserResponseDto> getUsers() {
        return this.userService.getUsers()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "201")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDto createUser(@RequestBody @Valid
    UserDto userDto) {
        var user = this.userService.createUser(userDto);
        return new UserResponseDto(user);
    }

    @Operation(summary = "Update user", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserResponseDto updateUser(@PathVariable("id")
    long id, @RequestBody @Valid
    UserDto userDto) {
        var user = this.userService.updateUser(id, userDto);
        return new UserResponseDto(user);
    }

    @Operation(summary = "Delete user", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(path = ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(@PathVariable("id")
    long id) {
        this.userService.deleteUser(id);
    }
}
