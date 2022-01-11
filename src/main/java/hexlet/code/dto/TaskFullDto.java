package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskFullDto extends TaskDto {
    private long authorId;

    public TaskFullDto(TaskDto taskDto, long authorId) {
        this.authorId = authorId;
        this.setName(taskDto.getName());
        this.setDescription(taskDto.getDescription());
        this.setTaskStatusId(taskDto.getTaskStatusId());
        this.setExecutorId(taskDto.getExecutorId());
    }
}
