package eg.mos.sportify.dto.competition;

import eg.mos.sportify.domain.enums.CompetitionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


/**
 * Data Transfer Object (DTO) for creating a new competition.

 * This DTO contains the necessary fields to create a competition, including its
 * name, description, start and end dates, maximum score, status, and the ID of the user
 * who is creating the competition.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetitionRequestDTO {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxScore;
    private CompetitionStatus status = CompetitionStatus.UPCOMING;
}
