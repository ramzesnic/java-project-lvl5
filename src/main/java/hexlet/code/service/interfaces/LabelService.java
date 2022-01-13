package hexlet.code.service.interfaces;

import java.util.List;
import java.util.Optional;

import hexlet.code.dto.LabelDto;
import hexlet.code.entity.Label;

public interface LabelService {
    Optional<Label> getLabel(long id);

    List<Label> getAllLabels();

    Label createLabel(LabelDto labelDto);

    Label updateLabel(long id, LabelDto labelDto);

    void deleteLabel(long id);
}
