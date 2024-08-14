package eg.mos.sportify.controller;


import eg.mos.sportify.dto.competition.AddCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/competitions")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addCompetition(@RequestBody AddCompetitionDTO addCompetitionDTO) {
        return ResponseEntity.ok(this.competitionService.addCompetition(addCompetitionDTO));
    }
}
