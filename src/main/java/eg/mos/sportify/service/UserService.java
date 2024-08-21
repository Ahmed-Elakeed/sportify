package eg.mos.sportify.service;

import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.PlayerCompetition;
import eg.mos.sportify.domain.Profile;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.user.UserAuthenticationDTO;
import eg.mos.sportify.dto.user.UserCompetitionReport;
import eg.mos.sportify.dto.user.UserRegistrationDTO;
import eg.mos.sportify.dto.user.UserSearchDTO;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.PlayerCompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.repository.specefication.UserSpecification;
import eg.mos.sportify.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlayerCompetitionRepository playerCompetitionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public ApiResponse<String> userLogin(UserAuthenticationDTO userAuthenticationDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userAuthenticationDTO.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (this.passwordEncoder.matches(userAuthenticationDTO.getPassword(), user.getPassword())) {
                return ApiResponse.<String>builder()
                        .success(true)
                        .message("Valid credentials, Your ID : " + user.getUserId() + " and the token attached to the response")
                        .data(jwtTokenProvider.generateToken(userAuthenticationDTO))
                        .build();
            }
        }
        return ApiResponse.<String>builder()
                .success(false)
                .message("Invalid credentials")
                .data("Wrong username or password")
                .build();
    }

    public ApiResponse<String> register(UserRegistrationDTO userRegistrationDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userRegistrationDTO.getUsername());
        if (optionalUser.isPresent()) {
            return ApiResponse.<String>builder()
                    .success(false)
                    .message("Username is already in use")
                    .data("Invalid username")
                    .build();
        } else {
            User user = this.userRepository.save(
                    User.builder()
                            .username(userRegistrationDTO.getUsername())
                            .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                            .email(userRegistrationDTO.getEmail())
                            .profilePicture(userRegistrationDTO.getProfilePicture())
                            .profile(
                                    Profile.builder()
                                            .firstName(userRegistrationDTO.getFirstName())
                                            .lastName(userRegistrationDTO.getLastName())
                                            .bio(userRegistrationDTO.getBio())
                                            .dateOfBirth(userRegistrationDTO.getDateOfBirth())
                                            .location(userRegistrationDTO.getLocation())
                                            .gender(userRegistrationDTO.getGender())
                                            .phone(userRegistrationDTO.getPhone())
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

    public ApiResponse<User> getUserProfile(Long userId) {
        return ApiResponse.<User>builder()
                .success(true)
                .message("User profile")
                .data(this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found")))
                .build();
    }

    public ApiResponse<List<User>> searchUsers(UserSearchDTO userSearchDTO) {
        Specification<User> userSpecification = new UserSpecification(userSearchDTO);
        return ApiResponse.<List<User>>builder()
                .success(true)
                .message("Search results")
                .data(this.userRepository.findAll(userSpecification))
                .build();
    }

    public ApiResponse<UserCompetitionReport> getUserCompetitionReport(Long userId) {
        List<PlayerCompetition> playerCompetitionList = this.playerCompetitionRepository.findAllByPlayerUserId(userId);
        if(playerCompetitionList.isEmpty()) {
            throw new NotFoundException("This player does not have any competitions");
        }
        UserCompetitionReport userCompetitionReport = new UserCompetitionReport();
        userCompetitionReport.constructReport(playerCompetitionList);
        return ApiResponse.<UserCompetitionReport>builder()
                .success(true)
                .message("User competition report")
                .data(userCompetitionReport)
                .build();
    }
}
