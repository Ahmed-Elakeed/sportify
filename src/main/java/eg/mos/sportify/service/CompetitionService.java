package eg.mos.sportify.service;

import eg.mos.sportify.domain.AuditData;
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
import eg.mos.sportify.security.AuthUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


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

        validateUserAuthorization(user);

        Competition competition = createCompetition(addCompetitionDTO, user);
        competitionRepository.save(competition);

        eventPublisher.publishEvent(new CompetitionAddedEvent(this, competition.getCompetitionId(), addCompetitionDTO.getUserId(), PlayerRole.ORGANIZER));

        return buildSuccessResponse("Competition added successfully.", "User with ID: " + addCompetitionDTO.getUserId() + " added competition successfully.");
    }

    /**
     * Validates that the authenticated user is allowed to add a competition.
     *
     * @param user the user to validate.
     * @throws AuthorizationException if the authenticated user is not the same as the user attempting to add the competition.
     */
    private void validateUserAuthorization(User user) {
        String currentAuthenticatedUsername = AuthUserDetailsService.getUsernameFromToken();
        if (!Objects.equals(currentAuthenticatedUsername, user.getUsername())) {
            throw new AuthorizationException("User can only add competitions for themselves.");
        }
    }

    /**
     * Creates a Competition entity from the provided DTO and user.
     *
     * @param addCompetitionDTO the DTO containing competition details.
     * @param user the user who is adding the competition.
     * @return a newly created Competition entity.
     */
    private Competition createCompetition(AddCompetitionDTO addCompetitionDTO, User user) {
        return Competition.builder()
                .name(addCompetitionDTO.getName())
                .description(addCompetitionDTO.getDescription())
                .startDate(addCompetitionDTO.getStartDate())
                .endDate(addCompetitionDTO.getEndDate())
                .maxScore(addCompetitionDTO.getMaxScore())
                .status(addCompetitionDTO.getStatus())
                .admin(user)
                .auditData(new AuditData())
                .build();
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

        validateAdminAuthorization(competition);

        competition.setStatus(competitionChangeStatusDTO.getStatus());
        competition.setEndDate(LocalDateTime.now()); // Example of updating the end date when status changes
        competitionRepository.save(competition);

        return buildSuccessResponse("Competition status changed successfully.", "Status changed to " + competitionChangeStatusDTO.getStatus());
    }

    /**
     * Validates that the authenticated user is the admin of the competition.
     *
     * @param competition the competition to validate.
     * @throws AuthorizationException if the authenticated user is not the admin of the competition.
     */
    private void validateAdminAuthorization(Competition competition) {
        if (!Objects.equals(competition.getAdmin().getUsername(), AuthUserDetailsService.getUsernameFromToken())) {
            throw new AuthorizationException("Only the admin of the competition can change the status.");
        }
    }

    /**
     * Builds a successful ApiResponse with a custom message and data.
     *
     * @param message the success message.
     * @param data additional data to return in the response.
     * @return a constructed ApiResponse.
     */
    private ApiResponse<String> buildSuccessResponse(String message, String data) {
        return ApiResponse.<String>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
}
