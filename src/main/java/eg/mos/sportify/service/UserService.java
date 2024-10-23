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


/**
 * Service class for managing user operations such as authentication, registration,
 * profile retrieval, and competition reporting.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlayerCompetitionRepository playerCompetitionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param userAuthenticationDTO the DTO containing user credentials.
     * @return an ApiResponse containing the JWT token or an error message.
     */
    public ApiResponse<String> userLogin(UserAuthenticationDTO userAuthenticationDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userAuthenticationDTO.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userAuthenticationDTO.getPassword(), user.getPassword())) {
                return buildSuccessResponse("Valid credentials. Your ID: " + user.getUserId(),
                        jwtTokenProvider.generateToken(userAuthenticationDTO));
            }
        }
        return buildErrorResponse("Invalid credentials", "Wrong username or password");
    }

    /**
     * Registers a new user.
     *
     * @param userRegistrationDTO the DTO containing user registration details.
     * @return an ApiResponse indicating success or failure of registration.
     */
    public ApiResponse<String> register(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.findByUsername(userRegistrationDTO.getUsername()).isPresent()) {
            return buildErrorResponse("Username is already in use", "Invalid username");
        }

        User user = createUser(userRegistrationDTO);
        userRepository.save(user);

        return buildSuccessResponse("User registered successfully",
                "You can log in now using your username and password");
    }

    /**
     * Retrieves the profile of a user by their ID.
     *
     * @param userId the ID of the user.
     * @return an ApiResponse containing the user's profile.
     * @throws NotFoundException if the user is not found.
     */
    public ApiResponse<User> getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return buildSuccessResponse("User profile", user);
    }

    /**
     * Searches for users based on specified criteria.
     *
     * @param userSearchDTO the DTO containing search criteria.
     * @return an ApiResponse containing a list of matching users.
     */
    public ApiResponse<List<User>> searchUsers(UserSearchDTO userSearchDTO) {
        Specification<User> userSpecification = new UserSpecification(userSearchDTO);
        List<User> users = userRepository.findAll(userSpecification);
        return buildSuccessResponse("Search results", users);
    }

    /**
     * Generates a competition report for a user.
     *
     * @param userId the ID of the user.
     * @return an ApiResponse containing the user's competition report.
     * @throws NotFoundException if the user has no associated competitions.
     */
    public ApiResponse<UserCompetitionReport> getUserCompetitionReport(Long userId) {
        List<PlayerCompetition> playerCompetitionList = playerCompetitionRepository.findAllByPlayerUserId(userId);
        if (playerCompetitionList.isEmpty()) {
            throw new NotFoundException("This player does not have any competitions");
        }
        UserCompetitionReport userCompetitionReport = new UserCompetitionReport();
        userCompetitionReport.constructReport(playerCompetitionList);
        return buildSuccessResponse("User competition report", userCompetitionReport);
    }

    /**
     * Creates a new User instance from the registration DTO.
     *
     * @param userRegistrationDTO the DTO containing user registration details.
     * @return a new User instance.
     */
    private User createUser(UserRegistrationDTO userRegistrationDTO) {
        return User.builder()
                .username(userRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .email(userRegistrationDTO.getEmail())
                .profilePicture(userRegistrationDTO.getProfilePicture())
                .profile(createProfile(userRegistrationDTO))
                .auditData(createAuditData())
                .build();
    }

    /**
     * Creates a Profile instance from the registration DTO.
     *
     * @param userRegistrationDTO the DTO containing user registration details.
     * @return a new Profile instance.
     */
    private Profile createProfile(UserRegistrationDTO userRegistrationDTO) {
        return Profile.builder()
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .bio(userRegistrationDTO.getBio())
                .dateOfBirth(userRegistrationDTO.getDateOfBirth())
                .location(userRegistrationDTO.getLocation())
                .gender(userRegistrationDTO.getGender())
                .phone(userRegistrationDTO.getPhone())
                .auditData(createAuditData())
                .build();
    }

    /**
     * Creates a new AuditData instance with the current timestamp.
     *
     * @return a new AuditData instance.
     */
    private AuditData createAuditData() {
        return AuditData.builder()
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Builds a successful ApiResponse with a custom message and data.
     *
     * @param message the success message.
     * @param data additional data to return in the response.
     * @return a constructed ApiResponse.
     */
    private <T> ApiResponse<T> buildSuccessResponse(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Builds an error ApiResponse with a custom message and data.
     *
     * @param message the error message.
     * @param data additional error details to return in the response.
     * @return a constructed ApiResponse.
     */
    private <T> ApiResponse<T> buildErrorResponse(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }
}
