package eg.mos.sportify.service;

import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.user.UserAuthenticationDTO;
import eg.mos.sportify.dto.user.UserRequestDTO;
import eg.mos.sportify.dto.user.UserResponseDTO;
import eg.mos.sportify.dto.user.UserSearchDTO;
import eg.mos.sportify.exception.NotFoundException;

import java.util.List;

public interface UserService {
    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param userAuthenticationDTO the DTO containing user credentials.
     * @return an ApiResponse containing the JWT token or an error message.
     */
    ApiResponse<String> userLogin(UserAuthenticationDTO userAuthenticationDTO);

    /**
     * Registers a new user.
     *
     * @param userRequestDTO the DTO containing user registration details.
     * @return an ApiResponse indicating success or failure of registration.
     */
    ApiResponse<UserResponseDTO> register(UserRequestDTO userRequestDTO);

    /**
     * Retrieves the profile of a user by their ID.
     *
     * @param userId the ID of the user.
     * @return an ApiResponse containing the user's profile.
     * @throws NotFoundException if the user is not found.
     */
    ApiResponse<UserResponseDTO> getUserProfile(Long userId) throws NotFoundException;

    /**
     * Searches for users based on specified criteria.
     *
     * @param userSearchDTO the DTO containing search criteria.
     * @return an ApiResponse containing a list of matching users.
     */
    ApiResponse<List<UserResponseDTO>> searchUsers(UserSearchDTO userSearchDTO);
}
