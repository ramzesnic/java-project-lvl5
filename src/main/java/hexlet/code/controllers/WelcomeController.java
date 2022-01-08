package hexlet.code.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("welcome")
public final class WelcomeController {
    public static final String WELCOME_CONTROLLER_PATH = "welcome";

    @GetMapping
    public String welcome() {
        return "Welcome to Spring";
    }
}
