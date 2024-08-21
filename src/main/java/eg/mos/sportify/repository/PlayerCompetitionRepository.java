package eg.mos.sportify.repository;

import eg.mos.sportify.domain.PlayerCompetition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerCompetitionRepository extends JpaRepository<PlayerCompetition, Long> {
    @Query(value = "select pc from PlayerCompetition pc where pc.player.userId =:userId and pc.competition.competitionId =:competitionId")
    Optional<PlayerCompetition> findByUserIdAndCompetitionId(@Param(value = "userId") Long userId,@Param(value = "competitionId") Long competitionId);

    List<PlayerCompetition> findAllByPlayerUserId(Long userId);
}
