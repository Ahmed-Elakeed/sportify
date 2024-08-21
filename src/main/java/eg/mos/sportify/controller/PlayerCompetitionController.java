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

@RestController
@RequestMapping("/player-competition")
@RequiredArgsConstructor
public class PlayerCompetitionController {

    private final PlayerCompetitionService playerCompetitionService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addPlayerToCompetition(@RequestBody AddPlayerCompetitionDTO addPlayerCompetitionDTO) {
        return ResponseEntity.ok(this.playerCompetitionService.addPlayerToCompetition(addPlayerCompetitionDTO));
    }

    @PutMapping("/score")
    public ResponseEntity<ApiResponse<String>> updatePLayerScore(@RequestBody @Valid PlayerScoreDTO playerScoreDTO){
        return ResponseEntity.ok(this.playerCompetitionService.updatePlayerScore(playerScoreDTO));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> removePlayerFromCompetition(@RequestBody RemovePlayerFromCompetitionDTO removePlayerFromCompetitionDTO){
        return ResponseEntity.ok(this.playerCompetitionService.removePlayerFromCompetition(removePlayerFromCompetitionDTO));
    }

}
