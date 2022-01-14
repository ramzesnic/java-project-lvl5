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
import org.springframework.transaction.annotation.Transactional;

import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.LabelResponseDto;
import hexlet.code.util.TestUtils;

@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet(value = {"labels.yml", "users.yml"}, cleanAfter = true)
public class LabelControllerTest {
    private static final String TEST_URL = "/api/labels";
    private static final String LABEL_NAME = "testLabel";
    private static final int LABEL_COUNT = 1;

    @Autowired
    private TestUtils utils;

    @Test
    void testGetAllLabels() throws Exception {
        var response = utils.makeSecureResponse(get(TEST_URL));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        var labels = this.utils.readJSON(response.getContentAsString(),
                new TypeReference<List<LabelResponseDto>>() {
                });
        assertThat(labels.size()).isEqualTo(LABEL_COUNT);
    }

    @Test
    void testGetLabel() throws Exception {
        var response = utils.makeSecureResponse(get(TEST_URL + "/1"));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

        var labelRo = this.utils.readJSON(response.getContentAsString(),
                new TypeReference<LabelResponseDto>() {
                });
        assertThat(labelRo.getName()).isEqualTo(LABEL_NAME);
    }

    @Test
    void testCreateLabel() throws Exception {
        var label = this.makeLabelDto();
        var content = this.utils.writeJson(label);
        var response = utils.makeSecureResponse(
                post(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var labelRo = this.utils.readJSON(response.getContentAsString(),
                new TypeReference<LabelResponseDto>() {
                });

        assertThat(labelRo.getName()).isEqualTo(label.getName());
    }

    @Test
    void testUpdateLabel() throws Exception {
        var label = this.makeLabelDto();
        var content = this.utils.writeJson(label);
        var response = utils.makeSecureResponse(put(TEST_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        var labelRo = this.utils.readJSON(response.getContentAsString(),
                new TypeReference<LabelResponseDto>() {
                });

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(labelRo.getName()).isEqualTo(label.getName());
    }

    @Test
    void testDeleteLabel() throws Exception {
        var response = utils.makeSecureResponse(
                delete(TEST_URL + "/1"));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private LabelDto makeLabelDto() {
        return new LabelDto("testName");
    }
}
