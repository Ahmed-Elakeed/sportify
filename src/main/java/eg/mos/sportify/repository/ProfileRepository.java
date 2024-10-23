package eg.mos.sportify.repository;

import eg.mos.sportify.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for managing Profile entities.
 * This interface extends JpaRepository, providing CRUD operations
 * and additional query methods for the Profile entity.
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
