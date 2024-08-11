package eg.mos.sportify.dto;

import eg.mos.sportify.domain.enums.CompetitionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCompetitionDTO {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private CompetitionStatus status;
    private Long userId;
}
