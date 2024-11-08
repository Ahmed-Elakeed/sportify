package eg.mos.sportify.service;

import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.competition.CompetitionRequestDTO;
import eg.mos.sportify.dto.competition.CompetitionChangeStatusDTO;
import eg.mos.sportify.dto.competition.CompetitionResponseDTO;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.exception.AuthorizationException;

public interface CompetitionService {

    /**
     * Adds a new competition to the system.
     *
     * @param competitionRequestDTO the DTO containing details of the competition to be added
     * @return an {@link ApiResponse} indicating the success or failure of the operation
     * @throws NotFoundException if the user specified in the DTO does not exist
     * @throws AuthorizationException if the authenticated user is not authorized to add the competition
     */
    ApiResponse<CompetitionResponseDTO> addCompetition(CompetitionRequestDTO competitionRequestDTO);

    /**
     * Changes the status of an existing competition.
     *
     * @param competitionChangeStatusDTO the DTO containing the competition ID and the new status
     * @return an {@link ApiResponse} indicating the success or failure of the operation
     * @throws NotFoundException if the competition with the specified ID does not exist
     * @throws AuthorizationException if the authenticated user is not authorized to change the status of the competition
     */
    ApiResponse<CompetitionResponseDTO> changeCompetitionStatus(CompetitionChangeStatusDTO competitionChangeStatusDTO);
}
