package hexlet.code.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.config.security.interfaces.ExtendUserDetails;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskFullDto;
import hexlet.code.dto.TaskResponseDto;
import hexlet.code.service.interfaces.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("${base-url}" + "tasks")
public class TaskController {
    private static final String ID = "/{id}";
    public static final String TASK_CONTROLLER_PATH = "users";
    private static final String ONLY_OWNER_BY_ID = """
            @taskServiceImpl.getTask(#id).get().getId() == principal.getUserId()
            """;

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Get all tasks", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return this.taskService.getAllTasks()
                .stream()
                .map(TaskResponseDto::new)
                .collect(Collectors.toList());
    }

    @Operation(summary = "get task by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = ID)
    public TaskResponseDto getTask(@PathVariable("id")
    long id) {
        return this.taskService.getTask(id)
                .map(TaskResponseDto::new)
                .orElseThrow();
    }

    @Operation(summary = "Create task", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskResponseDto createTask(Authentication auth,
            @RequestBody @Valid
            TaskDto taskDto) {
        var userDetails = (ExtendUserDetails) auth.getPrincipal();
        var fullDto = new TaskFullDto(taskDto, userDetails.getUserId());
        var task = this.taskService.createTask(fullDto);
        return new TaskResponseDto(task);
    }

    @Operation(summary = "Update test", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = ID)
    public TaskResponseDto updateTask(@PathVariable("id")
    long id,
            @RequestBody @Valid
            TaskDto taskDto) {
        var task = this.taskService.updateTask(id, taskDto);
        return new TaskResponseDto(task);
    }

    @Operation(summary = "Delete task", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping(path = ID)
    public void deleteTask(@PathVariable("id")
    long id) {
        this.taskService.deleteTask(id);
    }
}
