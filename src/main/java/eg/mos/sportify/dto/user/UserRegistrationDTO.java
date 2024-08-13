package eg.mos.sportify.dto.user;


import eg.mos.sportify.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
