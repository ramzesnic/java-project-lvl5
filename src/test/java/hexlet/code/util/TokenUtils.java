package hexlet.code.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import hexlet.code.service.interfaces.AuthService;

@Service
public class TokenUtils {
    private static final String BEARER_FORMAT = "Bearer %s";
    private static final String EXIST_USER_EMAIL = "test@Email.com";
    private static final String PASSWORD = "testpassword1";

    @Autowired
    private AuthService authService;

    public MockHttpServletRequestBuilder addTokenToRequest(MockHttpServletRequestBuilder builder) {
        String token = authService.login(EXIST_USER_EMAIL, PASSWORD);
        String bearer = String.format(BEARER_FORMAT, token);
        builder.header(HttpHeaders.AUTHORIZATION, bearer);
        return builder;
    }
}
