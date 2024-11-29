package eg.mos.sportify.service;


import eg.mos.sportify.domain.Profile;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.Gender;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.dto.user.UserAuthenticationDTO;
import eg.mos.sportify.dto.user.UserRequestDTO;
import eg.mos.sportify.dto.user.UserResponseDTO;
import eg.mos.sportify.dto.user.UserSearchDTO;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testUserRegistration_Success() {
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
                        .build())
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .userId(1L)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .profile(ProfileDTO.builder()
                        .phone("test")
                        .bio("test")
                        .location("test")
                        .dateOfBirth("test")
                        .gender(Gender.MALE)
                        .lastName("test")
                        .firstName("test")
                        .profilePicture("test")
                        .build())
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .build();
        ApiResponse<UserResponseDTO> registerUserResponse = userService.register(userRequestDTO);


        assertEquals(registerUserResponse.getData().getUsername(), "test");
        assertTrue(registerUserResponse.isSuccess());
    }


    @Test
    public void testUserRegistration_Fail() {
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
                        .build())
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .userId(1L)
                .build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .profile(ProfileDTO.builder()
                        .phone("test")
                        .bio("test")
                        .location("test")
                        .dateOfBirth("test")
                        .gender(Gender.MALE)
                        .lastName("test")
                        .firstName("test")
                        .profilePicture("test")
                        .build())
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .build();
        ApiResponse<UserResponseDTO> registerUserResponse = userService.register(userRequestDTO);

        assertFalse(registerUserResponse.isSuccess());
    }

    @Test
    public void testUserLogin_Success() {
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
                        .build())
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .userId(1L)
                .build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any())).thenReturn(true);

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder().username("test").password("test").build();

        ApiResponse<String> userLoginResponse = userService.userLogin(userAuthenticationDTO);

        assertTrue(userLoginResponse.isSuccess());
    }

    @Test
    public void testUserLogin_Fail() {
        when(userRepository.findByUsername("not_exist_user")).thenReturn(Optional.empty());
        when(passwordEncoder.matches(any(String.class), any())).thenReturn(false);

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder().username("not_exist_user").password("test").build();

        ApiResponse<String> userLoginResponse = userService.userLogin(userAuthenticationDTO);

        assertFalse(userLoginResponse.isSuccess());
    }

    @Test
    public void testGetUserProfile() {
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
                        .build())
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .userId(1L)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ApiResponse<UserResponseDTO> userResponseDTO = userService.getUserProfile(1L);

        assertTrue(userResponseDTO.isSuccess());
        assertEquals(userResponseDTO.getData().getUsername(), "test");
    }

    @Test
    public void testGetUserProfileFail() {
        when(userRepository.findById(1000L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserProfile(1000L));
    }

    @Test
    public void testSearchUsers() {
        List<User> users = new ArrayList<>(Arrays.asList(
                User.builder().userId(1L).profile(Profile.builder().firstName("test").build()).build(),
                User.builder().userId(2L).profile(Profile.builder().firstName("test").build()).build()
        ));
        when(userRepository.findAll(any(Specification.class))).thenAnswer(invocation -> users);

        ApiResponse<List<UserResponseDTO>> userResponseDTOs = userService.searchUsers(UserSearchDTO.builder().firstName("test").build());
        assertEquals(userResponseDTOs.getData().size(), 2);
        assertTrue(userResponseDTOs.isSuccess());
    }
}
