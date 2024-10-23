package eg.mos.sportify.service;


import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.PlayerCompetition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import eg.mos.sportify.dto.player_competition.PlayerScoreDTO;
import eg.mos.sportify.dto.player_competition.AddPlayerCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.player_competition.RemovePlayerFromCompetitionDTO;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.exception.ValidationException;
import eg.mos.sportify.repository.PlayerCompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service class for managing player participation in competitions.
 * Provides methods to add, update, and remove players from competitions.
 */
@Service
@RequiredArgsConstructor
public class PlayerCompetitionService {


    private final PlayerCompetitionRepository playerCompetitionRepository;
    private final UserRepository userRepository;

    /**
     * Adds a player to a competition.
     *
     * @param addPlayerCompetitionDTO the DTO containing the details of the player and competition.
     * @return an ApiResponse indicating success or failure.
     * @throws NotFoundException if the specified player or competition does not exist.
     * @throws AuthorizationException if the player cannot be added to the competition due to status.
     */
    public ApiResponse<String> addPlayerToCompetition(AddPlayerCompetitionDTO addPlayerCompetitionDTO) {
        User user = getCurrentUser();
        Competition competition = getCompetitionById(user, addPlayerCompetitionDTO.getCompetitionId());

        validateCompetitionStatus(competition);

        User newPlayer = userRepository.findById(addPlayerCompetitionDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Player not found"));

        PlayerCompetition playerCompetition = playerCompetitionRepository.save(
                PlayerCompetition.builder()
                        .competition(competition)
                        .player(newPlayer)
                        .role(addPlayerCompetitionDTO.getRole())
                        .score(0)
                        .auditData(new AuditData())
                        .build()
        );

        return buildSuccessResponse("Player added to competition successfully.", "The new record ID: " + playerCompetition.getPlayerCompetitionId());
    }

    /**
     * Updates the score of a player in a competition.
     *
     * @param playerScoreDTO the DTO containing the player's ID, competition ID, and new score.
     * @return an ApiResponse indicating success or failure.
     * @throws NotFoundException if the specified player or competition does not exist.
     * @throws ValidationException if the score exceeds the maximum allowed score.
     */
    public ApiResponse<String> updatePlayerScore(PlayerScoreDTO playerScoreDTO) {
        User user = getCurrentUser();
        Competition competition = getCompetitionById(user, playerScoreDTO.getCompetitionId());

        PlayerCompetition playerCompetition = playerCompetitionRepository
                .findByUserIdAndCompetitionId(playerScoreDTO.getUserId(), playerScoreDTO.getCompetitionId())
                .orElseThrow(() -> new NotFoundException("This player is not assigned to this competition"));

        validateScore(playerScoreDTO.getScore(), competition.getMaxScore());

        playerCompetition.setScore(playerScoreDTO.getScore());
        playerCompetition.setAuditData(AuditData.builder()
                .createdAt(playerCompetition.getAuditData().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build()
        );
        playerCompetitionRepository.save(playerCompetition);

        return buildSuccessResponse("Player score updated successfully.", "The new record ID: " + playerCompetition.getPlayerCompetitionId());
    }

    /**
     * Removes a player from a competition.
     *
     * @param removePlayerFromCompetitionDTO the DTO containing the player ID and competition ID.
     * @return an ApiResponse indicating success or failure.
     * @throws NotFoundException if the specified player or competition does not exist.
     * @throws AuthorizationException if the user does not have permission to remove the player.
     */
    public ApiResponse<String> removePlayerFromCompetition(RemovePlayerFromCompetitionDTO removePlayerFromCompetitionDTO) {
        PlayerCompetition playerCompetition = playerCompetitionRepository
                .findByUserIdAndCompetitionId(removePlayerFromCompetitionDTO.getUserId(), removePlayerFromCompetitionDTO.getCompetitionId())
                .orElseThrow(() -> new NotFoundException("This player is not assigned to this competition"));

        User user = getCurrentUser();
        validateRemovalPermission(playerCompetition, user);

        playerCompetitionRepository.delete(playerCompetition);
        return buildSuccessResponse("Player removed from competition successfully.", "Deleted Successfully");
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the authenticated User.
     * @throws NotFoundException if the user cannot be found.
     */
    private User getCurrentUser() {
        return userRepository.findByUsername(AuthUserDetailsService.getUsernameFromToken())
                .orElseThrow(() -> new NotFoundException("Competition organizer not found"));
    }

    /**
     * Retrieves a competition by its ID, ensuring the user has authorization to access it.
     *
     * @param user the authenticated user.
     * @param competitionId the ID of the competition.
     * @return the Competition if found and authorized.
     * @throws AuthorizationException if the user is not authorized to access the competition.
     */
    private Competition getCompetitionById(User user, Long competitionId) {
        return user.getCreatedCompetitions()
                .stream()
                .filter(c -> c.getCompetitionId().equals(competitionId))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("You are not authorized to access this competition"));
    }

    /**
     * Validates the status of the competition.
     *
     * @param competition the competition to validate.
     * @throws AuthorizationException if the competition is not upcoming.
     */
    private void validateCompetitionStatus(Competition competition) {
        if (competition.getStatus() != CompetitionStatus.UPCOMING) {
            throw new AuthorizationException("You can only add players to upcoming competitions.");
        }
    }

    /**
     * Validates the player's score against the maximum score allowed for the competition.
     *
     * @param score the player's score.
     * @param maxScore the maximum allowed score for the competition.
     * @throws ValidationException if the score exceeds the maximum score.
     */
    private void validateScore(int score, int maxScore) {
        if (score > maxScore) {
            throw new ValidationException("Score must be less than or equal to the maximum score of " + maxScore);
        }
    }

    /**
     * Validates if the user has permission to remove a player from a competition.
     *
     * @param playerCompetition the player competition record to validate.
     * @param user the authenticated user.
     * @throws AuthorizationException if the user does not have permission to remove the player.
     */
    private void validateRemovalPermission(PlayerCompetition playerCompetition, User user) {
        if (!playerCompetition.getPlayer().getUserId().equals(user.getUserId()) && !playerCompetition.getCompetition().getAdmin().getUserId().equals(user.getUserId())) {
            throw new AuthorizationException("You do not have permission to remove this player from the competition.");
        }
    }

    /**
     * Builds a successful ApiResponse with a custom message and data.
     *
     * @param message the success message.
     * @param data additional data to return in the response.
     * @return a constructed ApiResponse.
     */
    private ApiResponse<String> buildSuccessResponse(String message, String data) {
        return ApiResponse.<String>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }


}
