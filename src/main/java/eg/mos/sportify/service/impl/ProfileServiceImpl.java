package eg.mos.sportify.service.impl;


import eg.mos.sportify.mapper.ProfileMapper;
import eg.mos.sportify.domain.Profile;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.ProfileRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import eg.mos.sportify.service.ProfileService;
import eg.mos.sportify.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * Service class for managing user profiles.
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;


    @Override
    public ApiResponse<ProfileDTO> updateUserProfile(Long userId, ProfileDTO profileDTO) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getUsername().equals(AuthUserDetailsService.getUsernameFromToken())){
                Profile oldProfile = user.getProfile();
                Profile newProfile = ProfileMapper.profileDTOTOProfile(profileDTO);
                newProfile.setProfileId(oldProfile.getProfileId());
                newProfile.setAuditData(oldProfile.getAuditData());
                newProfile.getAuditData().setUpdatedAt(LocalDateTime.now());
                return ApiResponseUtil.buildSuccessResponse(
                        "User Profile Updated successfully", ProfileMapper.profileTOProfileDTO(this.profileRepository.save(newProfile))
                );
            }
            throw new AuthorizationException("User can update his own profile only");
        }
        throw new NotFoundException("User not found");
    }
}
