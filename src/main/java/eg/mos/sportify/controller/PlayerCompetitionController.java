package eg.mos.sportify.controller;


import eg.mos.sportify.dto.player_competition.PlayerScoreRequestDTO;
import eg.mos.sportify.dto.player_competition.PlayerCompetitionRequestDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.user.UserCompetitionReport;
import eg.mos.sportify.service.PlayerCompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * REST controller for managing player participation in competitions.

 * This controller provides endpoints to add players to competitions, update their scores,
 * and remove players from competitions.
 */
@RestController
@RequestMapping("/player-competition")
@RequiredArgsConstructor
public class PlayerCompetitionController {

    private final PlayerCompetitionService playerCompetitionService;


    /**
     * Endpoint to add a player to a competition.

     * This method accepts an {@link PlayerCompetitionRequestDTO} object that contains the
     * necessary details to add a player to a competition.
     *
     * @param playerCompetitionRequestDTO DTO containing player and competition details.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addPlayerToCompetition(@RequestBody PlayerCompetitionRequestDTO playerCompetitionRequestDTO) {
        return ResponseEntity.ok(this.playerCompetitionService.addPlayerToCompetition(playerCompetitionRequestDTO));
    }


    /**
     * Endpoint to update a player's score in a competition.

     * This method accepts a {@link PlayerScoreRequestDTO} object that contains the player's ID
     * and the new score, and it updates the player's score in the competition.
     *
     * @param playerScoreRequestDTO DTO containing player ID and the new score.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @PutMapping("/score")
    public ResponseEntity<ApiResponse<String>> updatePlayerScore(@RequestBody @Valid PlayerScoreRequestDTO playerScoreRequestDTO){
        return ResponseEntity.ok(this.playerCompetitionService.updatePlayerScore(playerScoreRequestDTO));
    }


    /**
     * Endpoint to remove a player from a competition.

     * This method accepts a {@link PlayerCompetitionRequestDTO} object that contains
     * the player's ID and competition details, and it removes the player from the specified competition.
     *
     * @param playerCompetitionRequestDTO DTO containing player ID and competition details.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> removePlayerFromCompetition(@RequestBody PlayerCompetitionRequestDTO playerCompetitionRequestDTO){
        return ResponseEntity.ok(this.playerCompetitionService.removePlayerFromCompetition(playerCompetitionRequestDTO));
    }


    /**
     * Endpoint to retrieve a user's competition report.

     * This method accepts a user ID as a path variable and returns the user's competition
     * report wrapped in an {@link ApiResponse}.
     *
     * @param userId ID of the user whose competition report is to be retrieved.
     * @return ResponseEntity containing an {@link ApiResponse} with the user's competition report.
     */
    @GetMapping("/{userId}/competition-report")
    public ResponseEntity<ApiResponse<UserCompetitionReport>> getUserCompetitionReport(@PathVariable Long userId) {
        return ResponseEntity.ok(this.playerCompetitionService.getUserCompetitionReport(userId));
    }

}
