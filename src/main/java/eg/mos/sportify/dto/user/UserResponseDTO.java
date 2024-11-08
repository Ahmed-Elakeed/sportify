package eg.mos.sportify.dto.user;

import eg.mos.sportify.dto.profile.ProfileDTO;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private ProfileDTO profile;
}
