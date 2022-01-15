package hexlet.code.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import hexlet.code.entity.QTask;
import hexlet.code.entity.Task;

@Repository
public interface TaskRepository extends
        JpaRepository<Task, Long>,
        QuerydslPredicateExecutor<Task>,
        QuerydslBinderCustomizer<QTask> {
    @Override
    default void customize(QuerydslBindings bindings, QTask task) {
        bindings.bind(task.taskStatus.id)
                .firstOptional((path, value) -> Optional.of(path.eq(value.get())));
        bindings.bind(task.executor.id)
                .firstOptional((path, value) -> Optional.of(path.eq(value.get())));
        bindings.bind(task.author.id)
                .firstOptional((path, value) -> Optional.of(path.eq(value.get())));
        bindings.bind(task.labels.any().id)
                .firstOptional((path, value) -> Optional.of(path.eq(value.get())));
    }
}
