package hexlet.code.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskFullDto;
import hexlet.code.entity.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.interfaces.TaskService;
import hexlet.code.service.interfaces.TaskStatusService;
import hexlet.code.service.interfaces.UserService;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskStatusService statusService;

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    @Override
    public Optional<Task> getTask(long id) {
        return this.taskRepository.findById(id);
    }

    @Override
    public Task createTask(TaskFullDto taskDto) {
        var status = this.statusService.getStatus(taskDto.getTaskStatusId())
                .orElseThrow();
        var author = this.userService.getUser(taskDto.getAuthorId())
                .orElseThrow();
        var executor = Optional.ofNullable(taskDto.getExecutorId())
                .map(this.userService::getUser)
                .orElse(null);
        var labels = this.labelRepository.findAllById(taskDto.getLabelIds());
        var task = new Task();

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(status);
        task.setAuthor(author);
        task.setExecutor(executor.get());
        task.setLabels(labels);

        return this.taskRepository.save(task);
    }

    @Override
    public Task updateTask(long id, TaskDto taskDto) {
        var task = this.taskRepository.findById(id)
                .orElseThrow();
        var status = this.statusService.getStatus(taskDto.getTaskStatusId())
                .orElseThrow();
        var executor = Optional.ofNullable(taskDto.getExecutorId())
                .map(this.userService::getUser)
                .map(user -> user.orElse(null))
                .orElse(task.getExecutor());
        var labels = this.labelRepository.findAllById(taskDto.getLabelIds());

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(status);
        task.setExecutor(executor);
        task.setLabels(labels);

        return this.taskRepository.save(task);
    }

    @Override
    public void deleteTask(long id) {
        this.taskRepository.deleteById(id);
    }
}
