package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.UserDto;
import hexlet.code.dto.UserResponseDto;
import hexlet.code.util.TestUtils;

@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet(value = "users.yml", cleanAfter = true)
public class UserControllerTest {
    private static final String TEST_URL = "/api/users";
    private static final String EXIST_USER_EMAIL = "test@Email.com";
    private static final int USER_COUNT = 1;

    @Autowired
    private TestUtils utils;

    @Test
    void testGetAllUsers() throws Exception {
        var response = this.utils.makeResponse(get(TEST_URL));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        var users = this.utils.readJSON(
                response.getContentAsString(),
                new TypeReference<List<UserResponseDto>>() {
                });

        assertThat(users.size()).isEqualTo(USER_COUNT);
    }

    @Test
    void testGetUserById() throws Exception {
        var response = utils.makeSecureResponse(get(TEST_URL + "/1"));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        assertThat(response.getContentAsString()).contains(EXIST_USER_EMAIL);

    }

    @Test
    void testCreateUser() throws Exception {
        var userDto = this.makeUserDto();
        var content = this.utils.writeJson(userDto);
        var response = this.utils.makeResponse(
                post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void testCreateUserValidation() throws Exception {
        var userDto = new UserDto("", "", "qwerttyy", "q");
        var content = this.utils.writeJson(userDto);
        var responsePost = this.utils.makeResponse(
                post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        assertThat(responsePost.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void testUpdateUser() throws Exception {
        var userDto = this.makeUserDto();
        var content = this.utils.writeJson(userDto);
        var response = utils.makeSecureResponse(put(TEST_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void deleteUser() throws Exception {
        var response = utils.makeSecureResponse(delete(TEST_URL + "/1"));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private UserDto makeUserDto() {
        // TODO add faker lib
        UserDto userDto = new UserDto("fname", "lname", "m@mail.com", "password");
        return userDto;
    }
}
