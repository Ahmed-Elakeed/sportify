package eg.mos.sportify.controller;


import eg.mos.sportify.domain.Profile;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.AuthRequest;
import eg.mos.sportify.dto.UserRegistrationRequest;
import eg.mos.sportify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(this.userService.userLogin(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(this.userService.register(userRegistrationRequest));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<Profile>> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(this.userService.getUserProfile(userId));
    }
}
