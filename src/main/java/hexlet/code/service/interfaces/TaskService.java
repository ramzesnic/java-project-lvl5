package hexlet.code.service.interfaces;

import java.util.List;
import java.util.Optional;

import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskFullDto;
import hexlet.code.entity.Task;

public interface TaskService {
    Optional<Task> getTask(long id);

    List<Task> getAllTasks();

    Task createTask(TaskFullDto taskDto);

    Task updateTask(long id, TaskDto taskDto);

    void deleteTask(long id);
}
