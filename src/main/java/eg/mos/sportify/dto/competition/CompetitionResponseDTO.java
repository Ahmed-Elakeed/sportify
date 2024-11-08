package eg.mos.sportify.dto.competition;

import eg.mos.sportify.domain.enums.CompetitionStatus;
import lombok.*;


import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CompetitionResponseDTO {
    private Long competitionId;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxScore;
    private CompetitionStatus status;
}
