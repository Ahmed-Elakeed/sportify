package eg.mos.sportify.dto.user;

import eg.mos.sportify.domain.enums.CompetitionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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
