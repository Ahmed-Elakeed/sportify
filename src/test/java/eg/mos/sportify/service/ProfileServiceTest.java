package eg.mos.sportify.service;


import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Profile;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.Gender;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.ProfileRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProfileServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Test
    public void testUpdateUserProfile_Success() {
        User user = User.builder()
                .profile(Profile.builder()
                        .phone("test")
                        .bio("test")
                        .location("test")
                        .dateOfBirth("test")
                        .gender(Gender.MALE)
                        .lastName("test")
                        .firstName("test")
                        .profilePicture("test")
                        .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                        .build())
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .userId(1L)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getUsernameFromToken)
                    .thenReturn(user.getUsername());

            Profile profile = Profile.builder()
                    .phone("test")
                    .bio("test")
                    .location("test")
                    .dateOfBirth("test")
                    .gender(Gender.MALE)
                    .lastName("testUpdated")
                    .firstName("testUpdated")
                    .profilePicture("test")
                    .profileId(1L)
                    .build();
            when(profileRepository.save(any(Profile.class))).thenReturn(profile);

            ApiResponse<ProfileDTO> updateUserProfileResponse = profileService.updateUserProfile(1L,
                    ProfileDTO.builder()
                            .phone("test")
                            .bio("test")
                            .location("test")
                            .dateOfBirth("test")
                            .gender(Gender.MALE)
                            .lastName("testUpdated")
                            .firstName("testUpdated")
                            .profilePicture("test")
                            .build()
            );

            assertEquals(updateUserProfileResponse.getData().getFirstName(), "testUpdated");
            assertTrue(updateUserProfileResponse.isSuccess());
        }

    }

    @Test
    public void testUpdateUserProfile_Fail() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> profileService.updateUserProfile(1L, ProfileDTO.builder().build()));
    }
}
