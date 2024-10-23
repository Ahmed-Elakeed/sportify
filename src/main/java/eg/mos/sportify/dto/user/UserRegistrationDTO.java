package eg.mos.sportify.dto.user;


import eg.mos.sportify.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data Transfer Object (DTO) representing user registration information.

 * This class is used to encapsulate the data required for registering a new user
 * in the Sportify application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String dateOfBirth;
    private Gender gender;
    private String bio;
}
