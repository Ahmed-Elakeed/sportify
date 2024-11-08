package eg.mos.sportify.controller;


import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;


    @PutMapping(path = "/{userId}")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateUserProfile(@PathVariable(value = "userId") Long userId, @RequestBody ProfileDTO profileDTO){
        return ResponseEntity.ok(this.profileService.updateUserProfile(userId, profileDTO));
    }
}
