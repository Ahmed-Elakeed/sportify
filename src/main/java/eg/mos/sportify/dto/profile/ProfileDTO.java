package eg.mos.sportify.dto.profile;

import eg.mos.sportify.domain.enums.Gender;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProfileDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String dateOfBirth;
    private Gender gender;
    private String profilePicture;
    private String bio;
}
