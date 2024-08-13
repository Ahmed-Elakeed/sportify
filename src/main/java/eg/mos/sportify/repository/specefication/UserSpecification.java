package eg.mos.sportify.repository.specefication;

import eg.mos.sportify.domain.User;
import eg.mos.sportify.dto.user.UserSearchDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {

    private final UserSearchDTO userSearchDTO;

    public UserSpecification(UserSearchDTO useruserSearchDTO) {
        this.userSearchDTO = useruserSearchDTO;
    }

    @Override
    public Predicate toPredicate(@NotNull Root<User> root, @NotNull CriteriaQuery<?> criteriaQuery, @NotNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (userSearchDTO.getFirstName() != null && !userSearchDTO.getFirstName().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.join("profile").get("firstName"), "%" + userSearchDTO.getFirstName() + "%"));
        }
        if (userSearchDTO.getLastName() != null && !userSearchDTO.getLastName().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.join("profile").get("lastName"), "%" + userSearchDTO.getLastName() + "%"));
        }
        if (userSearchDTO.getPhone() != null && !userSearchDTO.getPhone().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.join("profile").get("phone"), "%" + userSearchDTO.getPhone() + "%"));
        }
        if (userSearchDTO.getEmail() != null && !userSearchDTO.getEmail().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("email"), "%" + userSearchDTO.getEmail() + "%"));
        }
        if (userSearchDTO.getUsername() != null && !userSearchDTO.getUsername().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("username"), "%" + userSearchDTO.getUsername() + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
