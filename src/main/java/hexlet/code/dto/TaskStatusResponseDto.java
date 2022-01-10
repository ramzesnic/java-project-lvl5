package hexlet.code.dto;

import java.util.Date;

import hexlet.code.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusResponseDto extends TaskStatusDto {
    private long id;
    private Date createdAt;

    public TaskStatusResponseDto(TaskStatus status) {
        this.id = status.getId();
        this.createdAt = status.getCreatedAt();
        this.setName(status.getName());
    }
}
