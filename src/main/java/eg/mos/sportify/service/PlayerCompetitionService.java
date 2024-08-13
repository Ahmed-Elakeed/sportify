package eg.mos.sportify.service;


import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.PlayerCompetition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.player_competition.AddPlayerCompetitionDTO;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.PlayerCompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerCompetitionService {

    private final PlayerCompetitionRepository playerCompetitionRepository;
    private final UserRepository userRepository;



    public ApiResponse<String> addPlayerToCompetition(AddPlayerCompetitionDTO addPlayerCompetitionDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(AuthUserDetailsService.getUsernameFromToken());
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("Competition organizer not found");
        }
        User user = optionalUser.get();
        Competition competition = user.getCreatedCompetitions()
                .stream()
                .filter(c -> c.getCompetitionId().equals(addPlayerCompetitionDTO.getCompetitionId()))
                .findFirst()
                .orElse(null);
        if (competition != null) {
            Optional<User> optionalNewPlayer = userRepository.findById(addPlayerCompetitionDTO.getUserId());
            if (optionalNewPlayer.isPresent()) {
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
            throw new NotFoundException("Player not found");
        }
        throw new AuthorizationException("You have no authorization to add player to this competition");

    }
}
