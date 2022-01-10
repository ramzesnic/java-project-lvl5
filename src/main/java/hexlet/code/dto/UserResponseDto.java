package hexlet.code.dto;

import java.util.Date;

import hexlet.code.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto extends UserDto {
    private long id;
    private Date createdAt;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.createdAt = user.getCreatedAt();
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
    }
}
