package hexlet.code.dto;

import hexlet.code.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto extends UserDto {
    private long id;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
    }
}
