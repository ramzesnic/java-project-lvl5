package hexlet.code.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.dto.AuthDto;
import hexlet.code.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("${base-url}" + "login")
public class AuthController {
    public static final String AUTH_CONTROLLER_PATH = "login";
    @Autowired
    private AuthService authService;

    @Operation(summary = "Login")
    @PostMapping
    public final String login(@RequestBody @Valid
    AuthDto authDto) {
        return this.authService.login(authDto.getEmail(), authDto.getPassword());
    }
}
