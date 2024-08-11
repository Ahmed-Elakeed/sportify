package eg.mos.sportify.event;

import eg.mos.sportify.domain.enums.PlayerRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CompetitionAddedEvent extends ApplicationEvent {
    private Long competitionId;
    private Long userId;
    private PlayerRole playerRole;

    public CompetitionAddedEvent(Object source, Long competitionId, Long userId, PlayerRole playerRole) {
        super(source);
        this.competitionId = competitionId;
        this.userId = userId;
        this.playerRole = playerRole;
    }
}
