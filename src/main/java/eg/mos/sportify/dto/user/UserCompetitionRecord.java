package eg.mos.sportify.dto.user;

import eg.mos.sportify.domain.enums.CompetitionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for representing a user's competition record.

 * This DTO contains details about a specific competition in which the user participated,
 * including the competition name, start and end dates, status, the user's score, and the winning ratio.
 */
@Data
@Builder
public class UserCompetitionRecord {
    private String competitionName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private CompetitionStatus status;
    private Integer playerScore;
    private Integer maxScore;
    private String winningRatio;
}
