package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static hexlet.code.config.SpringTestConfig.TEST_PROFILE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.TaskStatusResponseDto;
import hexlet.code.util.TestUtils;

@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet(value = {"task_statuses.yml", "users.yml"}, cleanAfter = true)
public class TaskStatusCotrollerTest {
    private static final String TEST_URL = "/api/statuses";
    private static final String STATUS_NAME = "test";
    private static final int STATUS_COUNT = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils utils;

    @Test
    void testGetAllStatuses() throws Exception {
        var builder = utils.addTokenToRequest(get(TEST_URL));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        var statuses = this.utils.readJSON(response.getContentAsString(),
                new TypeReference<List<TaskStatusResponseDto>>() {
                });
        assertThat(statuses.size()).isEqualTo(STATUS_COUNT);
    }

    @Test
    void testGetStatus() throws Exception {
        var builder = utils.addTokenToRequest(get(TEST_URL + "/1"));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        var statusRo = this.utils.readJSON(response.getContentAsString(),
                new TypeReference<TaskStatusResponseDto>() {
                });
        assertThat(statusRo.getName()).isEqualTo(STATUS_NAME);
    }

    @Test
    void testCreateStatus() throws Exception {
        var status = this.makeStatusDto();
        var content = this.utils.writeJson(status);
        var builder = utils.addTokenToRequest(
                post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var statusRo = this.utils.readJSON(response.getContentAsString(),
                new TypeReference<TaskStatusResponseDto>() {
                });

        assertThat(statusRo.getName()).isEqualTo(status.getName());
    }

    @Test
    void testUpdateStatus() throws Exception {
        var status = this.makeStatusDto();
        var content = this.utils.writeJson(status);
        var builder = utils.addTokenToRequest(put(TEST_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        var statusRo = this.utils.readJSON(response.getContentAsString(),
                new TypeReference<TaskStatusResponseDto>() {
                });

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusRo.getName()).isEqualTo(status.getName());
    }

    @Test
    void testDeleteStatus() throws Exception {
        var builder = utils.addTokenToRequest(
                delete(TEST_URL + "/1"));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private TaskStatusDto makeStatusDto() {
        return new TaskStatusDto("testName");
    }
}
