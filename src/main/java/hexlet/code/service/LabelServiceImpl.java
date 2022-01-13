package hexlet.code.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hexlet.code.dto.LabelDto;
import hexlet.code.entity.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.interfaces.LabelService;

@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Override
    public List<Label> getAllLabels() {
        return this.labelRepository.findAll();
    }

    @Override
    public Optional<Label> getLabel(long id) {
        return this.labelRepository.findById(id);
    }

    @Override
    public Label createLabel(LabelDto labelDto) {
        var label = new Label();
        label.setName(labelDto.getName());

        return this.labelRepository.save(label);
    }

    @Override
    public Label updateLabel(long id, LabelDto labelDto) {
        var label = this.labelRepository.findById(id)
                .orElseThrow();
        label.setName(labelDto.getName());

        return this.labelRepository.save(label);
    }

    @Override
    public void deleteLabel(long id) {
        this.labelRepository.deleteById(id);
    }
}
