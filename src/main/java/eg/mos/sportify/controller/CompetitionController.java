package eg.mos.sportify.controller;


import eg.mos.sportify.dto.competition.AddCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.competition.CompetitionChangeStatusDTO;
import eg.mos.sportify.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for managing competitions.

 * This controller handles requests related to adding new competitions
 * and changing the status of existing competitions.
 */
@RestController
@RequestMapping("/competitions")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;


    /**
     * Endpoint to add a new competition.

     * This method accepts a {@link AddCompetitionDTO} object and adds
     * the competition using the service layer.
     *
     * @param addCompetitionDTO DTO containing competition details.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addCompetition(@RequestBody AddCompetitionDTO addCompetitionDTO) {
        return ResponseEntity.ok(this.competitionService.addCompetition(addCompetitionDTO));
    }


    /**
     * Endpoint to change the status of an existing competition.

     * This method accepts a {@link CompetitionChangeStatusDTO} object
     * and updates the competition's status using the service layer.
     *
     * @param competitionChangeStatusDTO DTO containing the competition ID and the new status.
     * @return ResponseEntity containing an {@link ApiResponse} with a success message.
     */
    @PutMapping("/change-status")
    public ResponseEntity<ApiResponse<String>> changeCompetitionStatus(@RequestBody CompetitionChangeStatusDTO competitionChangeStatusDTO){
        return ResponseEntity.ok(this.competitionService.changeCompetitionStatus(competitionChangeStatusDTO));
    }
}
