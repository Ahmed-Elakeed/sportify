package eg.mos.sportify.mapper;

import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.dto.competition.CompetitionRequestDTO;
import eg.mos.sportify.dto.competition.CompetitionResponseDTO;

import java.time.LocalDateTime;


/**
 * Utility class for mapping between Competition domain objects and DTOs.
 * <p>
 * This class provides static methods to convert between {@link CompetitionRequestDTO},
 * {@link Competition}, and {@link CompetitionResponseDTO}.
 * </p>
 * <p>
 * The class is not meant to be instantiated.
 * </p>
 */
public class CompetitionMapper {


    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used as a utility class with static methods only.
     */
    private CompetitionMapper() {
    }

    /**
     * Maps a {@link CompetitionRequestDTO} to a {@link Competition}.
     *
     * @param competitionRequestDTO the DTO containing competition data from the client.
     * @return a {@link Competition} entity initialized with the data from the DTO.
     */
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

    /**
     * Maps a {@link Competition} entity to a {@link CompetitionResponseDTO}.
     *
     * @param competition the {@link Competition} entity containing competition data.
     * @return a {@link CompetitionResponseDTO} initialized with the data from the entity.
     */
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
