package eg.mos.sportify.service;

import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.player_competition.PlayerCompetitionRequestDTO;
import eg.mos.sportify.dto.player_competition.PlayerScoreRequestDTO;
import eg.mos.sportify.dto.user.UserCompetitionReport;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.ValidationException;

public interface PlayerCompetitionService {
    /**
     * Adds a player to a competition.
     *
     * @param playerCompetitionRequestDTO the DTO containing the details of the player and competition.
     * @return an ApiResponse indicating success or failure.
     * @throws NotFoundException      if the specified player or competition does not exist.
     * @throws AuthorizationException if the player cannot be added to the competition due to status.
     */
    ApiResponse<String> addPlayerToCompetition(PlayerCompetitionRequestDTO playerCompetitionRequestDTO);

    /**
     * Updates the score of a player in a competition.
     *
     * @param playerScoreRequestDTO the DTO containing the player's ID, competition ID, and new score.
     * @return an ApiResponse indicating success or failure.
     * @throws NotFoundException   if the specified player or competition does not exist.
     * @throws ValidationException if the score exceeds the maximum allowed score.
     */
    ApiResponse<String> updatePlayerScore(PlayerScoreRequestDTO playerScoreRequestDTO);

    /**
     * Removes a player from a competition.
     *
     * @param removePlayerFromCompetitionDTO the DTO containing the player ID and competition ID.
     * @return an ApiResponse indicating success or failure.
     * @throws NotFoundException      if the specified player or competition does not exist.
     * @throws AuthorizationException if the user does not have permission to remove the player.
     */
    ApiResponse<String> removePlayerFromCompetition(PlayerCompetitionRequestDTO removePlayerFromCompetitionDTO);

    /**
     * Generates a competition report for a user.
     *
     * @param userId the ID of the user.
     * @return an ApiResponse containing the user's competition report.
     * @throws NotFoundException if the user has no associated competitions.
     */
    ApiResponse<UserCompetitionReport> getUserCompetitionReport(Long userId);
}
