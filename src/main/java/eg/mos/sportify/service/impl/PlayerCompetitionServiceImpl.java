package eg.mos.sportify.service.impl;


import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.PlayerCompetition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.PlayerRole;
import eg.mos.sportify.dto.player_competition.PlayerScoreRequestDTO;
import eg.mos.sportify.dto.player_competition.PlayerCompetitionRequestDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.user.UserCompetitionReport;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.exception.ValidationException;
import eg.mos.sportify.repository.CompetitionRepository;
import eg.mos.sportify.repository.PlayerCompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import eg.mos.sportify.service.PlayerCompetitionService;
import eg.mos.sportify.util.ApiResponseUtil;
import eg.mos.sportify.validation.PlayerCompetitionValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing player participation in competitions.
 * Provides methods to add, update, and remove players from competitions.
 */
@Service
@RequiredArgsConstructor
public class PlayerCompetitionServiceImpl implements PlayerCompetitionService {


    private final PlayerCompetitionRepository playerCompetitionRepository;
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;

    @Override
    public ApiResponse<String> addPlayerToCompetition(PlayerCompetitionRequestDTO playerCompetitionRequestDTO) {
        User currentUser = AuthUserDetailsService.getCurrentUser();
        Competition competition = this.competitionRepository.findById(playerCompetitionRequestDTO.getCompetitionId())
                .orElseThrow(() -> new NotFoundException("Competition with ID : " + playerCompetitionRequestDTO.getCompetitionId() + " not found"));

        if (!competition.getAdmin().getUserId().equals(currentUser.getUserId()))
            throw new AuthorizationException("You do not have permission to add a player to that competition");

        Optional<PlayerCompetition> optionalPlayerCompetition = this.playerCompetitionRepository.findByUserIdAndCompetitionId(playerCompetitionRequestDTO.getUserId(), playerCompetitionRequestDTO.getCompetitionId());
        if (optionalPlayerCompetition.isPresent()) {
            throw new ValidationException("The player already in that competition");
        }

        PlayerCompetitionValidation.validateCompetitionStatus(competition);

        User newPlayer = userRepository.findById(playerCompetitionRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Player with ID : " + playerCompetitionRequestDTO.getUserId() + " not found"));

        PlayerCompetition playerCompetition = playerCompetitionRepository.save(
                PlayerCompetition.builder()
                        .competition(competition)
                        .player(newPlayer)
                        .role(currentUser.getUserId().equals(competition.getAdmin().getUserId()) && currentUser.getUserId().equals(playerCompetitionRequestDTO.getUserId()) ? PlayerRole.ORGANIZER : PlayerRole.PLAYER )
                        .score(0)
                        .auditData(AuditData.builder()
                                .createdAt(LocalDateTime.now())
                                .build())
                        .build()
        );

        return ApiResponseUtil.buildSuccessResponse("Player added to competition successfully.", "The new record ID: " + playerCompetition.getPlayerCompetitionId());
    }

    @Override
    public ApiResponse<String> updatePlayerScore(PlayerScoreRequestDTO playerScoreRequestDTO) {
        PlayerCompetition playerCompetition = playerCompetitionRepository
                .findByUserIdAndCompetitionId(playerScoreRequestDTO.getUserId(), playerScoreRequestDTO.getCompetitionId())
                .orElseThrow(() -> new NotFoundException("This player is not assigned to this competition"));

        User currentUser = AuthUserDetailsService.getCurrentUser();
        if (!playerCompetition.getCompetition().getAdmin().getUserId().equals(currentUser.getUserId()))
            throw new AuthorizationException("You do not have permission to update player details in that competition");

        PlayerCompetitionValidation.validateScore(playerScoreRequestDTO.getScore(), playerCompetition.getCompetition().getMaxScore());

        playerCompetition.setScore(playerScoreRequestDTO.getScore());
        playerCompetition.setAuditData(AuditData.builder()
                .createdAt(playerCompetition.getAuditData().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build()
        );
        playerCompetitionRepository.save(playerCompetition);

        return ApiResponseUtil.buildSuccessResponse("Player score updated successfully.", "The Record with ID: " + playerCompetition.getPlayerCompetitionId() + " updated successfully.");
    }

    @Override
    public ApiResponse<String> removePlayerFromCompetition(PlayerCompetitionRequestDTO removePlayerFromCompetitionDTO) {
        PlayerCompetition playerCompetition = playerCompetitionRepository
                .findByUserIdAndCompetitionId(removePlayerFromCompetitionDTO.getUserId(), removePlayerFromCompetitionDTO.getCompetitionId())
                .orElseThrow(() -> new NotFoundException("This player is not assigned to this competition"));

        User user = AuthUserDetailsService.getCurrentUser();
        PlayerCompetitionValidation.validateRemovalPermission(playerCompetition, user);

        playerCompetitionRepository.delete(playerCompetition);
        return ApiResponseUtil.buildSuccessResponse("Player removed from competition successfully.", "Deleted Successfully");
    }

    @Override
    public ApiResponse<UserCompetitionReport> getUserCompetitionReport(Long userId) {
        this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("This user is not found"));
        List<PlayerCompetition> playerCompetitionList = playerCompetitionRepository.findAllByPlayerUserId(userId);
        if (playerCompetitionList.isEmpty()) {
            throw new NotFoundException("This player does not have any competitions");
        }
        UserCompetitionReport userCompetitionReport = new UserCompetitionReport();
        userCompetitionReport.constructReport(playerCompetitionList);
        return ApiResponseUtil.buildSuccessResponse("User competition report", userCompetitionReport);
    }


}
