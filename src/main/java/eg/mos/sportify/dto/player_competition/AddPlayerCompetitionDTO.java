package eg.mos.sportify.dto.player_competition;


import eg.mos.sportify.domain.enums.PlayerRole;
import lombok.*;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPlayerCompetitionDTO {

    private Long competitionId;
    private Long userId;
    private PlayerRole role;
}