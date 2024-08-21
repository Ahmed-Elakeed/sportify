package eg.mos.sportify.dto.competition;

import eg.mos.sportify.domain.enums.CompetitionStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitionChangeStatusDTO {
    private Long competitionId;
    private CompetitionStatus status;
}
