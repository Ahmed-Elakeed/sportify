package eg.mos.sportify.mapper;

import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Profile;
import eg.mos.sportify.dto.profile.ProfileDTO;

import java.time.LocalDateTime;

public class ProfileMapper {

    private ProfileMapper() {
    }

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
