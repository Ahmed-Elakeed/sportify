package eg.mos.sportify.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


/**
 * Custom event class that represents the addition of a competition.
 * This event is published when a competition is added to the system.
 */
@Getter
@Setter
public class CompetitionAddedEvent extends ApplicationEvent {
    private Long competitionId;
    private Long userId;

    /**
     * Constructs a new CompetitionAddedEvent.
     *
     * @param source the object that published the event (typically the source of the event)
     * @param competitionId the ID of the competition that was added
     * @param userId the ID of the user associated with the competition
     */
    public CompetitionAddedEvent(Object source, Long competitionId, Long userId) {
        super(source);
        this.competitionId = competitionId;
        this.userId = userId;
    }
}
