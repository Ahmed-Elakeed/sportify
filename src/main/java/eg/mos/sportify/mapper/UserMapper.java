package eg.mos.sportify.mapper;


import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.user.UserRequestDTO;
import eg.mos.sportify.dto.user.UserResponseDTO;

import java.time.LocalDateTime;



/**
 * Utility class for mapping between User domain objects and DTOs.
 * <p>
 * This class provides static methods to convert between {@link UserRequestDTO},
 * {@link UserResponseDTO}, and {@link User}.
 * </p>
 * <p>
 * The class is designed to be used as a utility and is not meant for instantiation.
 * </p>
 */
public class UserMapper {

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used as a utility class with static methods only.
     */
    private UserMapper() {
    }


    /**
     * Converts a {@link UserRequestDTO} to a {@link User} entity.
     * <p>
     * This method maps the incoming data from a user request DTO to a
     * {@link User} entity, initializing fields such as username, password,
     * email, and profile. The profile is mapped using the {@link ProfileMapper}.
     * </p>
     *
     * @param userRequestDTO the DTO containing user data from the client.
     * @return a {@link User} entity initialized with the data from the DTO.
     */
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


    /**
     * Converts a {@link User} entity to a {@link UserResponseDTO}.
     * <p>
     * This method maps the data from a {@link User} entity to a
     * {@link UserResponseDTO}, providing a response structure containing
     * user details such as ID, username, email, and profile information.
     * The profile is mapped using the {@link ProfileMapper}.
     * </p>
     *
     * @param user the {@link User} entity containing user data.
     * @return a {@link UserResponseDTO} initialized with the data from the entity.
     */
    public static UserResponseDTO userToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profile(ProfileMapper.profileTOProfileDTO(user.getProfile()))
                .build();
    }
}
