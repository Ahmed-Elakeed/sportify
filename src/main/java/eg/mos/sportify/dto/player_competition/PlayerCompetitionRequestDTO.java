package eg.mos.sportify.dto.player_competition;


import lombok.*;

/**
 * Data Transfer Object (DTO) for adding a player to a competition.

 * This DTO contains the necessary fields to add a player, including the competition ID,
 * user ID, and the role of the user within the competition.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerCompetitionRequestDTO {
    private Long competitionId;
    private Long userId;
}
