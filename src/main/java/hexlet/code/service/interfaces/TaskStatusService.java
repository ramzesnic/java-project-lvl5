package hexlet.code.service.interfaces;

import java.util.List;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.TaskStatusResponseDto;

public interface TaskStatusService {
    TaskStatusResponseDto getStatus(long id);

    List<TaskStatusResponseDto> getStatuses();

    TaskStatusResponseDto createStatus(TaskStatusDto statusDto);

    TaskStatusResponseDto updateStatus(long id, TaskStatusDto statusDto);

    void deleteStatus(long id);
}
