package eg.mos.sportify.controller;


import eg.mos.sportify.dto.player_competition.PlayerScoreDTO;
import eg.mos.sportify.dto.player_competition.AddPlayerCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.player_competition.RemovePlayerFromCompetitionDTO;
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

     * This method accepts an {@link AddPlayerCompetitionDTO} object that contains the
     * necessary details to add a player to a competition.
     *
     * @param addPlayerCompetitionDTO DTO containing player and competition details.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addPlayerToCompetition(@RequestBody AddPlayerCompetitionDTO addPlayerCompetitionDTO) {
        return ResponseEntity.ok(this.playerCompetitionService.addPlayerToCompetition(addPlayerCompetitionDTO));
    }


    /**
     * Endpoint to update a player's score in a competition.

     * This method accepts a {@link PlayerScoreDTO} object that contains the player's ID
     * and the new score, and it updates the player's score in the competition.
     *
     * @param playerScoreDTO DTO containing player ID and the new score.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @PutMapping("/score")
    public ResponseEntity<ApiResponse<String>> updatePLayerScore(@RequestBody @Valid PlayerScoreDTO playerScoreDTO){
        return ResponseEntity.ok(this.playerCompetitionService.updatePlayerScore(playerScoreDTO));
    }


    /**
     * Endpoint to remove a player from a competition.

     * This method accepts a {@link RemovePlayerFromCompetitionDTO} object that contains
     * the player's ID and competition details, and it removes the player from the specified competition.
     *
     * @param removePlayerFromCompetitionDTO DTO containing player ID and competition details.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> removePlayerFromCompetition(@RequestBody RemovePlayerFromCompetitionDTO removePlayerFromCompetitionDTO){
        return ResponseEntity.ok(this.playerCompetitionService.removePlayerFromCompetition(removePlayerFromCompetitionDTO));
    }

}
