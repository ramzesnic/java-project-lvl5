package hexlet.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hexlet.code.entity.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
}
