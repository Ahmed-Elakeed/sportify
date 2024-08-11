package eg.mos.sportify.service;

import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Profile;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.AuthRequest;
import eg.mos.sportify.dto.UserRegistrationRequest;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public ApiResponse<String> userLogin(AuthRequest authRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(authRequest.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (this.passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                return ApiResponse.<String>builder()
                        .success(true)
                        .message("Valid credentials, Your ID : " + user.getUserId() + " and the token attached to the response")
                        .data(jwtTokenProvider.generateToken(authRequest))
                        .build();
            }
        }
        return ApiResponse.<String>builder()
                .success(false)
                .message("Invalid credentials")
                .data("Wrong username or password")
                .build();
    }

    public ApiResponse<String> register(UserRegistrationRequest userRegistrationRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(userRegistrationRequest.getUsername());
        if (optionalUser.isPresent()) {
            return ApiResponse.<String>builder()
                    .success(false)
                    .message("Username is already in use")
                    .data("Invalid username")
                    .build();
        } else {
            User user = this.userRepository.save(
                    User.builder()
                            .username(userRegistrationRequest.getUsername())
                            .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                            .email(userRegistrationRequest.getEmail())
                            .profilePicture(userRegistrationRequest.getProfilePicture())
                            .profile(
                                    Profile.builder()
                                            .firstName(userRegistrationRequest.getFirstName())
                                            .lastName(userRegistrationRequest.getLastName())
                                            .bio(userRegistrationRequest.getBio())
                                            .dateOfBirth(userRegistrationRequest.getDateOfBirth())
                                            .location(userRegistrationRequest.getLocation())
                                            .gender(userRegistrationRequest.getGender())
                                            .phone(userRegistrationRequest.getPhone())
                                            .auditData(
                                                    AuditData.builder()
                                                            .createdAt(LocalDateTime.now())
                                                            .build()
                                            )
                                            .build()
                            )
                            .auditData(
                                    AuditData.builder()
                                            .createdAt(LocalDateTime.now())
                                            .build()
                            )
                            .build()
            );
            this.userRepository.save(user);
            return ApiResponse.<String>builder()
                    .success(true)
                    .message("User registered successfully")
                    .data("You can login now using your username and password")
                    .build();
        }
    }

    public ApiResponse<Profile> getUserProfile(Long userId) {
        return ApiResponse.<Profile>builder()
                .success(true)
                .message("User profile")
                .data(this.userRepository.findById(userId).get().getProfile())
                .build();
    }
}
