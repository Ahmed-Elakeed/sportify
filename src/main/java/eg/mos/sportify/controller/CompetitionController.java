package eg.mos.sportify.controller;


import eg.mos.sportify.dto.competition.AddCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.competition.CompetitionChangeStatusDTO;
import eg.mos.sportify.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/competitions")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addCompetition(@RequestBody AddCompetitionDTO addCompetitionDTO) {
        return ResponseEntity.ok(this.competitionService.addCompetition(addCompetitionDTO));
    }

    @PutMapping("/change-status")
    public ResponseEntity<ApiResponse<String>> changeCompetitionStatus(@RequestBody CompetitionChangeStatusDTO competitionChangeStatusDTO){
        return ResponseEntity.ok(this.competitionService.changeCompetitionStatus(competitionChangeStatusDTO));
    }
}
