package eg.mos.sportify.service;

import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.PlayerRole;
import eg.mos.sportify.dto.competition.AddCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.event.CompetitionAddedEvent;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.CompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;


    public ApiResponse<String> addCompetition(AddCompetitionDTO addCompetitionDTO) {
        Optional<User> optionalUser = userRepository.findById(addCompetitionDTO.getUserId());
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User with ID : " + addCompetitionDTO.getUserId() + " not found");
        }
        String currentAuthenticatedUsername = AuthUserDetailsService.getUsernameFromToken();
        if (Objects.equals(currentAuthenticatedUsername, optionalUser.get().getUsername())) {
            Competition competition = this.competitionRepository.save(
                    Competition.builder()
                            .name(addCompetitionDTO.getName())
                            .description(addCompetitionDTO.getDescription())
                            .startDate(addCompetitionDTO.getStartDate())
                            .endDate(addCompetitionDTO.getEndDate())
                            .status(addCompetitionDTO.getStatus())
                            .admin(optionalUser.get())
                            .auditData(new AuditData())
                            .build()
            );
            this.eventPublisher.publishEvent(new CompetitionAddedEvent(this, competition.getCompetitionId(), addCompetitionDTO.getUserId(), PlayerRole.ORGANIZER));
            return ApiResponse.<String>builder()
                    .success(true)
                    .message("Competition added")
                    .data("User with ID : " + addCompetitionDTO.getUserId() + " added competition successfully")
                    .build();
        }
        throw new AuthorizationException("User can add competition to only himself");

    }
}
