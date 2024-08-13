package eg.mos.sportify.controller;



import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.user.UserAuthenticationDTO;
import eg.mos.sportify.dto.user.UserRegistrationDTO;
import eg.mos.sportify.dto.user.UserSearchDTO;
import eg.mos.sportify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserAuthenticationDTO userAuthenticationDTO) {
        return ResponseEntity.ok(this.userService.userLogin(userAuthenticationDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        return ResponseEntity.ok(this.userService.register(userRegistrationDTO));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<User>> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(this.userService.getUserProfile(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<User>>> searchUsers(@RequestBody UserSearchDTO userSearchDTO) {
        return ResponseEntity.ok(this.userService.searchUsers(userSearchDTO));
    }
}
