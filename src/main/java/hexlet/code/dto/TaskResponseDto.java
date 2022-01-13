package hexlet.code.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import hexlet.code.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private long id;
    private String name;
    private String description;
    private Date createdAt;
    private TaskStatusResponseDto taskStatus;
    private UserResponseDto author;
    private UserResponseDto executor;
    private List<LabelResponseDto> labels;

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.createdAt = task.getCreatedAt();
        this.taskStatus = new TaskStatusResponseDto(task.getTaskStatus());
        this.author = new UserResponseDto(task.getAuthor());
        this.executor = new UserResponseDto(task.getExecutor());
        this.labels = task.getLabels()
                .stream()
                .map(LabelResponseDto::new)
                .collect(Collectors.toList());
    }
}
