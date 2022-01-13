package hexlet.code.controllers;

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
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskResponseDto;
import hexlet.code.util.TestUtils;

import static hexlet.code.config.SpringTestConfig.TEST_PROFILE;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet(value = {"task_statuses.yml", "users.yml", "tasks.yml"}, cleanAfter = true)
public class TaskControllerTest {
    private static final String TEST_URL = "/api/tasks";
    private static final String EXIST_TASK_NAME = "testTask";
    private static final int TASK_COUNT = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils utils;

    @Test
    void testGetAllTasks() throws Exception {
        var builder = utils.addTokenToRequest(get(TEST_URL));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        var tasks = utils.readJSON(response.getContentAsString(),
                new TypeReference<List<TaskResponseDto>>() {
                });

        assertThat(tasks.size()).isEqualTo(TASK_COUNT);
    }

    @Test
    void testGetTask() throws Exception {
        var builder = utils.addTokenToRequest(get(TEST_URL + "/1"));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        var task = utils.readJSON(response.getContentAsString(),
                new TypeReference<TaskResponseDto>() {
                });

        assertThat(task.getName()).isEqualTo(EXIST_TASK_NAME);
    }

    @Test
    void createTask() throws Exception {
        var taskDto = makeTaskDto();
        var content = utils.writeJson(taskDto);
        var builder = utils.addTokenToRequest(
                post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var taskRo = utils.readJSON(response.getContentAsString(),
                new TypeReference<TaskResponseDto>() {
                });

        assertThat(taskRo.getName()).isEqualTo(taskDto.getName());
        assertThat(taskRo.getAuthor().getId()).isEqualTo(1);
    }

    @Test
    void updateTask() throws Exception {
        var taskDto = makeTaskDto();
        var content = utils.writeJson(taskDto);
        var builder = utils.addTokenToRequest(
                put(TEST_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var taskRo = utils.readJSON(response.getContentAsString(),
                new TypeReference<TaskResponseDto>() {
                });

        assertThat(taskRo.getName()).isEqualTo(taskDto.getName());
    }

    @Test
    void deleteTask() throws Exception {
        var builder = utils.addTokenToRequest(delete(TEST_URL + "/1"));
        var response = mockMvc
                .perform(builder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private TaskDto makeTaskDto() {
        return new TaskDto("testName2", "test description", 1, 1);
    }
}
