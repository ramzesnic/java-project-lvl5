package hexlet.code.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hexlet.code.entity.TaskStatus;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.interfaces.TaskStatusService;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {
    @Autowired
    private TaskStatusRepository statusRepository;

    @Override
    public List<TaskStatus> getStatuses() {
        return this.statusRepository.findAll();
    }

    @Override
    public Optional<TaskStatus> getStatus(long id) {
        return this.statusRepository.findById(id);
    }

    @Override
    public TaskStatus createStatus(TaskStatusDto statusDto) {
        var status = new TaskStatus();
        status.setName(statusDto.getName());

        return this.statusRepository.save(status);
    }

    @Override
    public TaskStatus updateStatus(long id, TaskStatusDto statusDto) {
        var status = this.statusRepository.findById(id)
                .orElseThrow();

        status.setName(statusDto.getName());

        return this.statusRepository.save(status);
    }

    @Override
    public void deleteStatus(long id) {
        this.statusRepository.deleteById(id);
    }
}
