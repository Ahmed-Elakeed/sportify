package eg.mos.sportify.controller;


import eg.mos.sportify.dto.AddPlayerCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.service.PlayerCompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player-competition")
@RequiredArgsConstructor
public class PlayerCompetitionController {

    private final PlayerCompetitionService playerCompetitionService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addPlayerToCompetition(@RequestBody AddPlayerCompetitionDTO addPlayerCompetitionDTO) {
        return ResponseEntity.ok(this.playerCompetitionService.addPlayerToCompetition(addPlayerCompetitionDTO));
    }

}
