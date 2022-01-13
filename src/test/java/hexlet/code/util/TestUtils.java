package hexlet.code.util;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import hexlet.code.service.interfaces.TokenService;

@Service
public class TestUtils {
    private static final String BEARER_FORMAT = "Bearer %s";
    private static final String EXIST_USER_EMAIL = "test@Email.com";
    private static final String AUTH_FIELD = "email";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    private ObjectMapper mapper = new ObjectMapper();

    public MockHttpServletRequestBuilder addTokenToRequest(MockHttpServletRequestBuilder builder) {
        String token = tokenService.expiring(Map.of(AUTH_FIELD, EXIST_USER_EMAIL));
        String bearer = String.format(BEARER_FORMAT, token);
        builder.header(HttpHeaders.AUTHORIZATION, bearer);
        return builder;
    }

    public MockHttpServletResponse makeSecureResponse(MockHttpServletRequestBuilder builder) throws Exception {
        String token = tokenService.expiring(Map.of(AUTH_FIELD, EXIST_USER_EMAIL));
        String bearer = String.format(BEARER_FORMAT, token);
        builder.header(HttpHeaders.AUTHORIZATION, bearer);
        return mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();
    }

    public <T> T readJSON(String data, TypeReference<T> obj) throws JsonProcessingException {
        return this.mapper.readValue(data, obj);
    }

    public String writeJson(Object value) throws JsonProcessingException {
        return this.mapper.writeValueAsString(value);
    }
}
