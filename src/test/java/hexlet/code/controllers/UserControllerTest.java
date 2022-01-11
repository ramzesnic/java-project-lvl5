package hexlet.code.controllers;

//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

//import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;

import static hexlet.code.config.SpringTestConfig.TEST_PROFILE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;

import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.UserDto;
import hexlet.code.dto.UserResponseDto;
//import hexlet.code.mock.MockAuthFilter;
//import hexlet.code.mock.WithMockCustomUser;
import hexlet.code.util.TestUtils;

@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet(value = "users.yml", transactional = true)
public class UserControllerTest {
    private static final String TEST_URL = "/api/users";
    private static final String EXIST_USER_EMAIL = "test@Email.com";
    private static final int USER_COUNT = 1;

    @Autowired
    private MockMvc mockMvc;
    //@Autowired
    //private WebApplicationContext context;
    @Autowired
    private TestUtils utils;
    //@Autowired
    //private FilterChainProxy chainProxy;
    //private ObjectMapper objectMapper = new ObjectMapper();

    //@BeforeAll
    //public void setup() {
    //mockMvc = MockMvcBuilders.webAppContextSetup(context)
    //.apply(springSecurity())
    //.build();
    //}

    @Test
    void testGetAllUsers() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(TEST_URL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        var users = this.utils.readJSON(
                response.getContentAsString(),
                new TypeReference<List<UserResponseDto>>() {
                });

        assertThat(users.size()).isEqualTo(USER_COUNT);
    }

    @Test
    //@WithMockCustomUser
    void testGetUserById() throws Exception {
        var builder = utils.addTokenToRequest(get(TEST_URL + "/1"));
        MockHttpServletResponse response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        assertThat(response.getContentAsString()).contains(EXIST_USER_EMAIL);

    }

    @Test
    // TODO mock WithSecurityContext
    void testCreateUser() throws Exception {
        UserDto userDto = this.makeUserDto();
        String content = this.utils.writeJson(userDto);
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(TEST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void testCreateUserValidation() throws Exception {
        UserDto userDto = new UserDto("", "", "qwerttyy", "q");
        String content = this.utils.writeJson(userDto);
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(TEST_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto userDto = this.makeUserDto();
        String content = this.utils.writeJson(userDto);
        var builder = utils.addTokenToRequest(put(TEST_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        MockHttpServletResponse responsePut = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(responsePut.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void deleteUser() throws Exception {
        var builder = utils.addTokenToRequest(delete(TEST_URL + "/1"));
        MockHttpServletResponse responseDelete = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(responseDelete.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private UserDto makeUserDto() {
        // TODO add faker lib
        UserDto userDto = new UserDto("fname", "lname", "m@mail.com", "password");
        return userDto;
    }
}
