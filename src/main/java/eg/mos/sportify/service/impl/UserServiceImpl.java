package eg.mos.sportify.service.impl;


import eg.mos.sportify.mapper.UserMapper;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.user.*;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.repository.specefication.UserSpecification;
import eg.mos.sportify.security.JwtTokenProvider;
import eg.mos.sportify.service.UserService;
import eg.mos.sportify.util.ApiResponseUtil;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
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


    @Override
    public ApiResponse<UserResponseDTO> register(UserRequestDTO userRequestDTO) {
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            return ApiResponseUtil.buildErrorResponse("Username is already in use", null);
        }

        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User user = UserMapper.userRequestDTOTOUser(userRequestDTO);
        User savedUser = userRepository.save(user);

        return ApiResponseUtil.buildSuccessResponse("User registered successfully, You can log in now using your username and password",
                UserMapper.userToUserResponseDTO(savedUser));
    }

    @Override
    public ApiResponse<UserResponseDTO> getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return ApiResponseUtil.buildSuccessResponse("User profile", UserMapper.userToUserResponseDTO(user));
    }


    @Override
    public ApiResponse<List<UserResponseDTO>> searchUsers(UserSearchDTO userSearchDTO) {
        Specification<User> userSpecification = new UserSpecification(userSearchDTO);
        List<User> users = userRepository.findAll(userSpecification);
        return ApiResponseUtil.buildSuccessResponse("Search results", users.stream().map(UserMapper::userToUserResponseDTO).toList());
    }


}
