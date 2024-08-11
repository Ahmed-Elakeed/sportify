package eg.mos.sportify.repository;

import eg.mos.sportify.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u from User u left join fetch u.profile where u.username=:username")
    Optional<User> findByUsername(@Param(value = "username") String username);

}
