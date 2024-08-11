package eg.mos.sportify.event;

import eg.mos.sportify.dto.AddPlayerCompetitionDTO;
import eg.mos.sportify.service.PlayerCompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CompetitionListener {

    private final PlayerCompetitionService playerCompetitionService;

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
