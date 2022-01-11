package hexlet.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hexlet.code.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
