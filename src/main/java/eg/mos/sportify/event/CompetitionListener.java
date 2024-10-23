package eg.mos.sportify.event;

import eg.mos.sportify.dto.player_competition.AddPlayerCompetitionDTO;
import eg.mos.sportify.service.PlayerCompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for competition-related events.
 * This component listens for {@link CompetitionAddedEvent} and processes it accordingly.
 */
@Component
@RequiredArgsConstructor
public class CompetitionListener {

    private final PlayerCompetitionService playerCompetitionService;

    /**
     * Handles the {@link CompetitionAddedEvent} after a competition is added.
     * This method is triggered when a competition is added, and it
     * adds the user to the competition using the PlayerCompetitionService.
     *
     * @param competitionAddedEvent the event containing competition details
     */
    @EventListener
    public void addCompetitionPostProcess(CompetitionAddedEvent competitionAddedEvent){
        this.playerCompetitionService.addPlayerToCompetition(
                AddPlayerCompetitionDTO.builder()
                        .competitionId(competitionAddedEvent.getCompetitionId())
                        .userId(competitionAddedEvent.getUserId())
                        .role(competitionAddedEvent.getPlayerRole())
                        .build()
        );
    }
}
