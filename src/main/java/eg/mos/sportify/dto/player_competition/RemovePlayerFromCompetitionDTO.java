package eg.mos.sportify.dto.player_competition;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemovePlayerFromCompetitionDTO {
    private Long competitionId;
    private Long userId;
}
