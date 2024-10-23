package eg.mos.sportify.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for user authentication.

 * This DTO contains the username and password required to authenticate a user during login.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthenticationDTO {
    private String username;
    private String password;
}
