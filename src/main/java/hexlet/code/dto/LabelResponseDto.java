package hexlet.code.dto;

import java.util.Date;

import hexlet.code.entity.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelResponseDto {
    private long id;
    private String name;
    private Date createdAt;

    public LabelResponseDto(Label label) {
        this.id = label.getId();
        this.name = label.getName();
        this.createdAt = label.getCreatedAt();
    }
}
