package hexlet.code.service.interfaces;

import java.util.List;
import java.util.Optional;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.entity.TaskStatus;

public interface TaskStatusService {
    Optional<TaskStatus> getStatus(long id);

    List<TaskStatus> getStatuses();

    TaskStatus createStatus(TaskStatusDto statusDto);

    TaskStatus updateStatus(long id, TaskStatusDto statusDto);

    void deleteStatus(long id);
}
