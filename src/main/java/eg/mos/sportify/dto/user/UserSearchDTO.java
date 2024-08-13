package eg.mos.sportify.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
}
