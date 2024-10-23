package eg.mos.sportify.repository;

import eg.mos.sportify.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


/**
 * Repository interface for managing User entities.
 * This interface extends JpaRepository for standard CRUD operations
 * and JpaSpecificationExecutor for enabling dynamic query generation.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Finds a User by their username, fetching the associated Profile entity.
     *
     * @param username the username of the User
     * @return an Optional containing the found User, or empty if not found
     */
    @Query(value = "select u from User u left join fetch u.profile where u.username=:username")
    Optional<User> findByUsername(@Param(value = "username") String username);
}
