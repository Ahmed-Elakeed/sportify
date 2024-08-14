package eg.mos.sportify.dto.player_competition;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Builder
public class PlayerScoreDTO {
    private Long userId;
    private Long competitionId;
    @Min(value = 0, message = "Score must be zero or more")
    private Integer score;
}
