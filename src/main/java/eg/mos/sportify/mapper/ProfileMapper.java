package eg.mos.sportify.mapper;

import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Profile;
import eg.mos.sportify.dto.profile.ProfileDTO;

import java.time.LocalDateTime;



/**
 * Utility class for mapping between Profile domain objects and DTOs.
 * <p>
 * This class provides static methods to convert between {@link ProfileDTO}
 * and {@link Profile}.
 * </p>
 * <p>
 * The class is designed to be used as a utility and is not meant for instantiation.
 * </p>
 */
public class ProfileMapper {

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used as a utility class with static methods only.
     */
    private ProfileMapper() {
    }


    /**
     * Maps a {@link ProfileDTO} to a {@link Profile}.
     *
     * @param profileDTO the DTO containing profile data from the client.
     * @return a {@link Profile} entity initialized with the data from the DTO.
     */
    public static Profile profileDTOTOProfile(ProfileDTO profileDTO) {
        return Profile.builder()
                .firstName(profileDTO.getFirstName())
                .lastName(profileDTO.getLastName())
                .bio(profileDTO.getBio())
                .phone(profileDTO.getPhone())
                .location(profileDTO.getLocation())
                .dateOfBirth(profileDTO.getDateOfBirth())
                .profilePicture(profileDTO.getProfilePicture())
                .gender(profileDTO.getGender())
                .auditData(AuditData.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();
    }


    /**
     * Maps a {@link Profile} entity to a {@link ProfileDTO}.
     *
     * @param profile the {@link Profile} entity containing profile data.
     * @return a {@link ProfileDTO} initialized with the data from the entity.
     */
    public static ProfileDTO profileTOProfileDTO(Profile profile) {
        return ProfileDTO.builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .bio(profile.getBio())
                .phone(profile.getPhone())
                .location(profile.getLocation())
                .dateOfBirth(profile.getDateOfBirth())
                .profilePicture(profile.getProfilePicture())
                .gender(profile.getGender())
                .build();
    }
}
