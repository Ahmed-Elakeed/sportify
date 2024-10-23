package eg.mos.sportify.dto.player_competition;

import lombok.Builder;
import lombok.Data;


/**
 * Data Transfer Object (DTO) for removing a player from a competition.

 * This DTO contains the necessary fields to remove a player, including the competition ID
 * and the user's ID.
 */
@Data
@Builder
public class RemovePlayerFromCompetitionDTO {
    private Long competitionId;
    private Long userId;
}
