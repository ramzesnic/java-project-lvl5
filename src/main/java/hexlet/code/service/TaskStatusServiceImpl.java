package hexlet.code.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hexlet.code.entity.TaskStatus;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.TaskStatusResponseDto;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.interfaces.TaskStatusService;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {
    @Autowired
    private TaskStatusRepository statusRepository;

    @Override
    public List<TaskStatusResponseDto> getStatuses() {
        return this.statusRepository.findAll()
                .stream()
                .map(TaskStatusResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public TaskStatusResponseDto getStatus(long id) {
        return this.statusRepository.findById(id)
                .map(TaskStatusResponseDto::new)
                .orElseThrow();
    }

    @Override
    public TaskStatusResponseDto createStatus(TaskStatusDto statusDto) {
        var status = new TaskStatus();
        status.setName(statusDto.getName());

        return new TaskStatusResponseDto(this.statusRepository.save(status));
    }

    @Override
    public TaskStatusResponseDto updateStatus(long id, TaskStatusDto statusDto) {
        var status = this.statusRepository.findById(id)
                .orElseThrow();

        status.setName(statusDto.getName());

        return new TaskStatusResponseDto(this.statusRepository.save(status));
    }

    @Override
    public void deleteStatus(long id) {
        this.statusRepository.deleteById(id);
    }
}
