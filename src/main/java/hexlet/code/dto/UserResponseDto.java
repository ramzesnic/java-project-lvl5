package hexlet.code.dto;

import java.util.Date;

import hexlet.code.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private long id;
    private Date createdAt;
    private String firstName;
    private String lastName;
    private String email;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.createdAt = user.getCreatedAt();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
