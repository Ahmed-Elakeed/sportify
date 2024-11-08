package eg.mos.sportify.controller;



import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.user.*;
import eg.mos.sportify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST controller for managing user-related operations.

 * This controller provides endpoints for user authentication, registration,
 * profile retrieval, user search, and competition reports.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * Endpoint for user login.

     * This method accepts a {@link UserAuthenticationDTO} object and returns an
     * {@link ApiResponse} containing a success message or token upon successful authentication.
     *
     * @param userAuthenticationDTO DTO containing user login credentials.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserAuthenticationDTO userAuthenticationDTO) {
        return ResponseEntity.ok(this.userService.userLogin(userAuthenticationDTO));
    }


    /**
     * Endpoint for user registration.

     * This method accepts a {@link UserRequestDTO} object and registers the user,
     * returning an {@link ApiResponse} with a success message upon completion.
     *
     * @param userRequestDTO DTO containing user registration details.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(this.userService.register(userRequestDTO));
    }


    /**
     * Endpoint to retrieve a user's profile.

     * This method accepts a user ID as a path variable and returns the user's profile
     * wrapped in an {@link ApiResponse}.
     *
     * @param userId ID of the user whose profile is to be retrieved.
     * @return ResponseEntity containing an {@link ApiResponse} with the user's profile.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(this.userService.getUserProfile(userId));
    }


    /**
     * Endpoint to search for users.

     * This method accepts a {@link UserSearchDTO} object in the request body and returns
     * a list of users that match the search criteria.
     *
     * @param userSearchDTO DTO containing search parameters for user search.
     * @return ResponseEntity containing an {@link ApiResponse} with a list of matching users.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> searchUsers(@RequestBody UserSearchDTO userSearchDTO) {
        return ResponseEntity.ok(this.userService.searchUsers(userSearchDTO));
    }

}
