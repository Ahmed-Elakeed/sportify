package eg.mos.sportify.mapper;


import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.user.UserRequestDTO;
import eg.mos.sportify.dto.user.UserResponseDTO;

import java.time.LocalDateTime;

public class UserMapper {

    private UserMapper() {
    }

    public static User userRequestDTOTOUser(UserRequestDTO userRequestDTO) {
        return User.builder()
                .username(userRequestDTO.getUsername())
                .password(userRequestDTO.getPassword())
                .email(userRequestDTO.getEmail())
                .profile(ProfileMapper.profileDTOTOProfile(userRequestDTO.getProfile()))
                .auditData(AuditData.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();
    }

    public static UserResponseDTO userToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profile(ProfileMapper.profileTOProfileDTO(user.getProfile()))
                .build();
    }
}
