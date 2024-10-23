package eg.mos.sportify.dto.user;

import lombok.Builder;
import lombok.Data;


/**
 * Data Transfer Object for searching user information.
 * This class encapsulates the user's search criteria.
 * It utilizes Lombok annotations to generate boilerplate code
 * such as getters, setters, and a builder.
 */
@Data
@Builder
public class UserSearchDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
}
