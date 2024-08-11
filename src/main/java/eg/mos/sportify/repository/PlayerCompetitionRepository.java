package eg.mos.sportify.repository;

import eg.mos.sportify.domain.PlayerCompetition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerCompetitionRepository extends JpaRepository<PlayerCompetition, Long> {
}
