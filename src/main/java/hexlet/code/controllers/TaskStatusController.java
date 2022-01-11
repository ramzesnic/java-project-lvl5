package hexlet.code.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.TaskStatusResponseDto;
import hexlet.code.service.interfaces.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("${base-url}" + "statuses")
public class TaskStatusController {
    private static final String ID = "/{id}";
    public static final String CONTROLLER_PATH = "users";

    @Autowired
    private TaskStatusService statusService;

    @Operation(summary = "Get all task statuses", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public List<TaskStatusResponseDto> getAllStatuses() {
        return this.statusService.getStatuses()
                .stream()
                .map(TaskStatusResponseDto::new)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get task status", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = ID)
    public TaskStatusResponseDto gitStatus(@PathVariable("id")
    long id) {
        return this.statusService.getStatus(id)
                .map(TaskStatusResponseDto::new)
                .orElseThrow();
    }

    @Operation(summary = "Create task status", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskStatusResponseDto createStatus(@RequestBody @Valid
    TaskStatusDto statusDto) {
        var status = this.statusService.createStatus(statusDto);
        return new TaskStatusResponseDto(status);
    }

    @Operation(summary = "Update task status", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = ID)
    public TaskStatusResponseDto updateStatus(@PathVariable("id")
    long id, @RequestBody
    TaskStatusDto statusDto) {
        var status = this.statusService.updateStatus(id, statusDto);
        return new TaskStatusResponseDto(status);
    }

    @Operation(summary = "Delete task status", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(path = ID)
    public void deleteStatus(@PathVariable("id")
    long id) {
        this.statusService.deleteStatus(id);
    }
}
