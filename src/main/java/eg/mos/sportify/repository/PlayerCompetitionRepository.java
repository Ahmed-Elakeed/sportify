package eg.mos.sportify.repository;

import eg.mos.sportify.domain.PlayerCompetition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


/**
 * Repository interface for managing PlayerCompetition entities.
 * This interface extends JpaRepository, providing CRUD operations
 * and additional query methods for the PlayerCompetition entity.
 */
public interface PlayerCompetitionRepository extends JpaRepository<PlayerCompetition, Long> {
    /**
     * Finds a PlayerCompetition by user ID and competition ID.
     *
     * @param userId        the ID of the user (player)
     * @param competitionId the ID of the competition
     * @return an Optional containing the found PlayerCompetition, or empty if not found
     */
    @Query(value = "select pc from PlayerCompetition pc where pc.player.userId =:userId and pc.competition.competitionId =:competitionId")
    Optional<PlayerCompetition> findByUserIdAndCompetitionId(
            @Param(value = "userId") Long userId,
            @Param(value = "competitionId") Long competitionId
    );

    /**
     * Retrieves a list of PlayerCompetition entities associated with a specific user ID.
     *
     * @param userId the ID of the user (player)
     * @return a list of PlayerCompetition entities for the specified user
     */
    List<PlayerCompetition> findAllByPlayerUserId(Long userId);
}
