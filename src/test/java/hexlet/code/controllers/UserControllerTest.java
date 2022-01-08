package hexlet.code.controllers;

//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

//import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;

import hexlet.code.dto.UserDto;
import hexlet.code.dto.UserResponseDto;
//import hexlet.code.mock.MockAuthFilter;
//import hexlet.code.mock.WithMockCustomUser;
import hexlet.code.util.TokenUtils;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet("users.yml")
public class UserControllerTest {
    private static final String TEST_URL = "/api/users";
    private static final String EXIST_USER_EMAIL = "test@Email.com";
    private static final int USER_COUNT = 2;

    @Autowired
    private MockMvc mockMvc;
    //@Autowired
    //private WebApplicationContext context;
    @Autowired
    private TokenUtils util;
    //@Autowired
    //private FilterChainProxy chainProxy;
    private ObjectMapper objectMapper = new ObjectMapper();

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

        List<UserResponseDto> users = objectMapper
                .readValue(
                        response.getContentAsString(),
                        new TypeReference<List<UserResponseDto>>() {
                        });

        assertThat(users.size()).isEqualTo(USER_COUNT);
    }

    @Test
    //@WithMockCustomUser
    void testGetUserById() throws Exception {
        var builder = util.addTokenToRequest(get(TEST_URL + "/1"));
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
        String content = objectMapper.writeValueAsString(userDto);
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
        String content = objectMapper.writeValueAsString(userDto);
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
        String content = objectMapper.writeValueAsString(userDto);
        var builder = util.addTokenToRequest(put(TEST_URL + "/1")
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
        var builder = util.addTokenToRequest(delete(TEST_URL + "/1"));
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