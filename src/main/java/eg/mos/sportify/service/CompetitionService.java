package eg.mos.sportify.service;


import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.PlayerRole;
import eg.mos.sportify.dto.competition.AddCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.competition.CompetitionChangeStatusDTO;
import eg.mos.sportify.event.CompetitionAddedEvent;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.CompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.util.ApiResponseUtil;
import eg.mos.sportify.validation.CompetitionValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * Service class for managing competitions.
 * Provides methods to add a competition and change its status.
 */
@Service
@RequiredArgsConstructor
public class CompetitionService {


    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Adds a new competition.
     *
     * @param addCompetitionDTO the DTO containing details of the competition to be added.
     * @return an ApiResponse indicating success or failure.
     * @throws NotFoundException if the user specified in the DTO does not exist.
     * @throws AuthorizationException if the authenticated user is not allowed to add the competition.
     */
    public ApiResponse<String> addCompetition(AddCompetitionDTO addCompetitionDTO) {
        User user = userRepository.findById(addCompetitionDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User with ID: " + addCompetitionDTO.getUserId() + " not found"));

        CompetitionValidation.validateUserAuthorization(user);

        Competition competition = Competition.createCompetition(addCompetitionDTO, user);
        competitionRepository.save(competition);

        eventPublisher.publishEvent(new CompetitionAddedEvent(this, competition.getCompetitionId(), addCompetitionDTO.getUserId(), PlayerRole.ORGANIZER));

        return ApiResponseUtil.buildSuccessResponse("Competition added successfully.", "User with ID: " + addCompetitionDTO.getUserId() + " added competition successfully.");
    }



    /**
     * Changes the status of an existing competition.
     *
     * @param competitionChangeStatusDTO the DTO containing the competition ID and new status.
     * @return an ApiResponse indicating success or failure.
     * @throws NotFoundException if the specified competition does not exist.
     * @throws AuthorizationException if the authenticated user is not the admin of the competition.
     */
    public ApiResponse<String> changeCompetitionStatus(CompetitionChangeStatusDTO competitionChangeStatusDTO) {
        Competition competition = competitionRepository.findById(competitionChangeStatusDTO.getCompetitionId())
                .orElseThrow(() -> new NotFoundException("Competition with ID: " + competitionChangeStatusDTO.getCompetitionId() + " not found"));

        CompetitionValidation.validateCompetitionAdminAuthorization(competition);

        competition.setStatus(competitionChangeStatusDTO.getStatus());
        competition.setEndDate(LocalDateTime.now()); // Example of updating the end date when status changes
        competitionRepository.save(competition);

        return ApiResponseUtil.buildSuccessResponse("Competition status changed successfully.", "Status changed to " + competitionChangeStatusDTO.getStatus());
    }


}
