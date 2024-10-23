package eg.mos.sportify.repository;

import eg.mos.sportify.domain.Competition;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for managing Competition entities.
 * This interface extends JpaRepository, providing CRUD operations
 * and additional query methods for the Competition entity.
 */
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
}
