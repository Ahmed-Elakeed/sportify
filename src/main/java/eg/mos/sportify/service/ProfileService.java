package eg.mos.sportify.service;

import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.exception.AuthorizationException;

public interface ProfileService {

    /**
     * Updates the profile information for a specific user.
     *
     * @param userId     the ID of the user whose profile is being updated
     * @param profileDTO the data transfer object containing the updated profile information
     * @return an {@link ApiResponse} containing the updated user profile details
     * @throws AuthorizationException if the user is not authorized to update this profile
     * @throws NotFoundException if the user with the specified ID does not exist
     */
    ApiResponse<ProfileDTO> updateUserProfile(Long userId, ProfileDTO profileDTO);
}
