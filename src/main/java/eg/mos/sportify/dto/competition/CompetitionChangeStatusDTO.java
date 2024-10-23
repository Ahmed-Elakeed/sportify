package eg.mos.sportify.dto.competition;

import eg.mos.sportify.domain.enums.CompetitionStatus;
import lombok.Builder;
import lombok.Data;


/**
 * Data Transfer Object (DTO) for updating the status of a competition.

 * This DTO contains the competition's ID and the new status that should be applied to it.
 */
@Data
@Builder
public class CompetitionChangeStatusDTO {
    private Long competitionId;
    private CompetitionStatus status;
}
