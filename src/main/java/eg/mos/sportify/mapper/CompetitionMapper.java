package eg.mos.sportify.mapper;

import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.dto.competition.CompetitionRequestDTO;
import eg.mos.sportify.dto.competition.CompetitionResponseDTO;

import java.time.LocalDateTime;

public class CompetitionMapper {

    private CompetitionMapper() {
    }

    public static Competition competitionRequestDTOTOCompetition(CompetitionRequestDTO competitionRequestDTO) {
        return Competition.builder()
                .name(competitionRequestDTO.getName())
                .description(competitionRequestDTO.getDescription())
                .startDate(competitionRequestDTO.getStartDate())
                .endDate(competitionRequestDTO.getEndDate())
                .maxScore(competitionRequestDTO.getMaxScore())
                .status(competitionRequestDTO.getStatus())
                .auditData(AuditData.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();
    }

    public static CompetitionResponseDTO competitionTOCompetitionResponseDTO(Competition competition) {
        return CompetitionResponseDTO.builder()
                .competitionId(competition.getCompetitionId())
                .name(competition.getName())
                .description(competition.getDescription())
                .startDate(competition.getStartDate())
                .endDate(competition.getEndDate())
                .maxScore(competition.getMaxScore())
                .status(competition.getStatus())
                .build();
    }
}
