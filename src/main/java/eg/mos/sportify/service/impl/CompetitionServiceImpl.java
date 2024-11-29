package eg.mos.sportify.service.impl;


import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.mapper.CompetitionMapper;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.competition.CompetitionRequestDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.competition.CompetitionChangeStatusDTO;
import eg.mos.sportify.dto.competition.CompetitionResponseDTO;
import eg.mos.sportify.event.CompetitionAddedEvent;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.CompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import eg.mos.sportify.service.CompetitionService;
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
public class CompetitionServiceImpl implements CompetitionService {


    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;



    @Override
    public ApiResponse<CompetitionResponseDTO> addCompetition(CompetitionRequestDTO competitionRequestDTO) {
        User user = userRepository.findByUsername(AuthUserDetailsService.getUsernameFromToken())
                .orElseThrow(() -> new AuthorizationException("Invalid token and User"));

        Competition competition = CompetitionMapper.competitionRequestDTOTOCompetition(competitionRequestDTO);
        competition.setAdmin(user);
        Competition savedCompetition = competitionRepository.save(competition);

        eventPublisher.publishEvent(new CompetitionAddedEvent(this, savedCompetition.getCompetitionId(),user.getUserId()));

        return ApiResponseUtil.buildSuccessResponse("User with ID: " + user.getUserId() + " added a competition successfully.", CompetitionMapper.competitionTOCompetitionResponseDTO(savedCompetition));
    }


    @Override
    public ApiResponse<CompetitionResponseDTO> changeCompetitionStatus(CompetitionChangeStatusDTO competitionChangeStatusDTO) {
        Competition competition = competitionRepository.findById(competitionChangeStatusDTO.getCompetitionId())
                .orElseThrow(() -> new NotFoundException("Competition with ID: " + competitionChangeStatusDTO.getCompetitionId() + " not found"));

        CompetitionValidation.validateCompetitionAdminAuthorization(competition);

        competition.setStatus(competitionChangeStatusDTO.getStatus());
        competition.getAuditData().setUpdatedAt(LocalDateTime.now());
        Competition updatedCompetition = competitionRepository.save(competition);

        return ApiResponseUtil.buildSuccessResponse("Competition status changed successfully.", CompetitionMapper.competitionTOCompetitionResponseDTO(updatedCompetition));
    }


}
