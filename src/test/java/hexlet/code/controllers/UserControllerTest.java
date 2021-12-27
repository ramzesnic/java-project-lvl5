package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import hexlet.code.dto.UserDto;
import hexlet.code.dto.UserResponseDto;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet("users.yml")
public class UserControllerTest {
    private static final String TEST_URL = "/users";
    private static final String EXIST_USER_EMAIL = "test@Email.com";
    private static final int USER_COUNT = 2;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

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
    void testgetUserById() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(TEST_URL + "/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        assertThat(response.getContentAsString()).contains(EXIST_USER_EMAIL);

    }

    @Test
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

        UserResponseDto userResponse = objectMapper.readValue(responsePost.getContentAsString(), UserResponseDto.class);

        MockHttpServletResponse response = mockMvc
                .perform(get(TEST_URL + "/" + userResponse.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(userDto.getEmail());
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
        MockHttpServletResponse responsePut = mockMvc
                .perform(
                        put(TEST_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn()
                .getResponse();

        assertThat(responsePut.getStatus()).isEqualTo(HttpStatus.OK.value());

        UserResponseDto userResponse = objectMapper.readValue(responsePut.getContentAsString(), UserResponseDto.class);

        MockHttpServletResponse response = mockMvc
                .perform(get(TEST_URL + "/" + userResponse.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(userDto.getEmail());
    }

    @Test
    void deleteUser() throws Exception {
        MockHttpServletResponse responseDelete = mockMvc
                .perform(delete(TEST_URL + "/1"))
                .andReturn()
                .getResponse();

        assertThat(responseDelete.getStatus()).isEqualTo(HttpStatus.OK.value());

        MockHttpServletResponse response = mockMvc
                .perform(get(TEST_URL + "/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private UserDto makeUserDto() {
        // TODO add faker lib
        UserDto userDto = new UserDto("fname", "lname", "m@mail.com", "password");
        return userDto;
    }
}
