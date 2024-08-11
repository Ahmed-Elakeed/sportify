package eg.mos.sportify.dto;


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
