package eg.mos.sportify.service;


import eg.mos.sportify.domain.PlayerCompetition;
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
import eg.mos.sportify.util.ApiResponseUtil;
import eg.mos.sportify.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
                return ApiResponseUtil.buildSuccessResponse("Valid credentials. Your ID: " + user.getUserId(),
                        jwtTokenProvider.generateToken(userAuthenticationDTO));
            }
        }
        return ApiResponseUtil.buildErrorResponse("Invalid credentials", "Wrong username or password");
    }

    /**
     * Registers a new user.
     *
     * @param userRegistrationDTO the DTO containing user registration details.
     * @return an ApiResponse indicating success or failure of registration.
     */
    public ApiResponse<String> register(UserRegistrationDTO userRegistrationDTO) {
        throw new RuntimeException("Not implemented");
//        LogUtil.getInstance(UserService.class).info("Start register user: " + userRegistrationDTO.getUsername());
//        if (userRepository.findByUsername(userRegistrationDTO.getUsername()).isPresent()) {
//            return ApiResponseUtil.buildErrorResponse("Username is already in use", "Invalid username");
//        }
//
//        User user = User.createUser(userRegistrationDTO, passwordEncoder);
//        userRepository.save(user);
//
//        return ApiResponseUtil.buildSuccessResponse("User registered successfully",
//                "You can log in now using your username and password");
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
        return ApiResponseUtil.buildSuccessResponse("User profile", user);
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
        return ApiResponseUtil.buildSuccessResponse("Search results", users);
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
        return ApiResponseUtil.buildSuccessResponse("User competition report", userCompetitionReport);
    }
}
