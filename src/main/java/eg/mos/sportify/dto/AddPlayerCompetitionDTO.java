package eg.mos.sportify.dto;


import eg.mos.sportify.domain.enums.PlayerRole;
import lombok.*;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
@Builder
public class AddPlayerCompetitionDTO extends ApplicationEvent {

    private Long competitionId;
    private Long userId;
    private PlayerRole role;

    public AddPlayerCompetitionDTO(Long competitionId, Long userId, PlayerRole role) {
        super("DefaultSource");
        this.competitionId = competitionId;
        this.userId = userId;
        this.role = role;
    }
}
