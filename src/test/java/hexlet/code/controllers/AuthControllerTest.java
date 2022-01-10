package hexlet.code.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;
import static hexlet.code.config.SpringTestConfig.TEST_PROFILE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.AuthDto;
import hexlet.code.util.TestUtils;

@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet("users.yml")
public class AuthControllerTest {
    private static final String TEST_URL = "/api/login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils utils;

    private AuthDto authDto = new AuthDto("test@Email.com", "testpassword1");

    @Test
    void testLogin() throws Exception {
        String content = this.utils.writeJson(this.authDto);
        MockHttpServletResponse response = mockMvc
                .perform(post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
