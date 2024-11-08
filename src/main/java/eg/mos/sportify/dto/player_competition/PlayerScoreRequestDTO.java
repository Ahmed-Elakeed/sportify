package eg.mos.sportify.dto.player_competition;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;


/**
 * Data Transfer Object (DTO) for updating the score of a player in a competition.

 * This DTO contains the necessary fields to update a player's score, including the user's ID,
 * the competition's ID, and the new score value.
 */
@Data
@Builder
public class PlayerScoreRequestDTO {
    private Long userId;
    private Long competitionId;
    @Min(value = 0, message = "Score must be zero or more")
    private Integer score;
}
