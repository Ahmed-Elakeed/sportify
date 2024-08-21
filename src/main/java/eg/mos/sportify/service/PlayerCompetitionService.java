package eg.mos.sportify.service;


import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.PlayerCompetition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import eg.mos.sportify.dto.player_competition.PlayerScoreDTO;
import eg.mos.sportify.dto.player_competition.AddPlayerCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.player_competition.RemovePlayerFromCompetitionDTO;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.PlayerCompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerCompetitionService {

    private final PlayerCompetitionRepository playerCompetitionRepository;
    private final UserRepository userRepository;


    public ApiResponse<String> addPlayerToCompetition(AddPlayerCompetitionDTO addPlayerCompetitionDTO) {
        User user = getCurrentUser();
        Competition competition = getCompetitionById(user, addPlayerCompetitionDTO.getCompetitionId());

        if(competition.getStatus() != CompetitionStatus.UPCOMING){
            throw new AuthorizationException("You can add player to upcoming competitions only");
        }

        Optional<User> optionalNewPlayer = userRepository.findById(addPlayerCompetitionDTO.getUserId());
        if (optionalNewPlayer.isEmpty()) {
            throw new NotFoundException("Player not found");
        }

        User newPlayer = optionalNewPlayer.get();
        PlayerCompetition playerCompetition = this.playerCompetitionRepository.save(
                PlayerCompetition.builder()
                        .competition(competition)
                        .player(newPlayer)
                        .role(addPlayerCompetitionDTO.getRole())
                        .score(0)
                        .auditData(new AuditData())
                        .build()
        );

        return ApiResponse.<String>builder()
                .success(true)
                .message("Player added to competition successfully")
                .data("The new record ID : " + playerCompetition.getPlayerCompetitionId())
                .build();
    }


    public ApiResponse<String> updatePlayerScore(PlayerScoreDTO playerScoreDTO) {
        User user = getCurrentUser();
        getCompetitionById(user, playerScoreDTO.getCompetitionId());

        Optional<PlayerCompetition> optionalPlayerCompetition = this.playerCompetitionRepository
                .findByUserIdAndCompetitionId(playerScoreDTO.getUserId(), playerScoreDTO.getCompetitionId());

        if (optionalPlayerCompetition.isEmpty()) {
            throw new NotFoundException("This player is not assigned to this competition");
        }

        PlayerCompetition playerCompetition = optionalPlayerCompetition.get();
        playerCompetition.setScore(playerScoreDTO.getScore());
        playerCompetition.setAuditData(AuditData.builder().createdAt(playerCompetition.getAuditData().getCreatedAt()).updatedAt(LocalDateTime.now()).build());
        this.playerCompetitionRepository.save(playerCompetition);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Player score updated successfully")
                .data("The new record ID : " + playerCompetition.getPlayerCompetitionId())
                .build();
    }

    public ApiResponse<String> removePlayerFromCompetition(RemovePlayerFromCompetitionDTO removePlayerFromCompetitionDTO) {
        Optional<PlayerCompetition> optionalPlayerCompetition = this.playerCompetitionRepository.findByUserIdAndCompetitionId(removePlayerFromCompetitionDTO.getUserId(), removePlayerFromCompetitionDTO.getCompetitionId());
        if (optionalPlayerCompetition.isEmpty()) {
            throw new NotFoundException("This player is not assigned to this competition");
        }
        PlayerCompetition playerCompetition = optionalPlayerCompetition.get();
        User user = getCurrentUser();
        if(playerCompetition.getPlayer().getUserId().equals(user.getUserId()) || this.getCompetitionById(user, removePlayerFromCompetitionDTO.getCompetitionId()) != null) {
            this.playerCompetitionRepository.delete(playerCompetition);
            return ApiResponse.<String>builder()
                    .success(true)
                    .message("Deleted Successfully")
                    .data("Player removed from competition successfully")
                    .build();
        }
        throw new AuthorizationException("You don't have permission to delete this competition");
    }

    private User getCurrentUser() {
        Optional<User> optionalUser = userRepository.findByUsername(AuthUserDetailsService.getUsernameFromToken());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Competition organizer not found");
        }
        return optionalUser.get();
    }

    private Competition getCompetitionById(User user, Long competitionId) {
        return user.getCreatedCompetitions()
                .stream()
                .filter(c -> c.getCompetitionId().equals(competitionId))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("You have no authorization to access this competition"));
    }


}
