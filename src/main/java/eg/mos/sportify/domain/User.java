package eg.mos.sportify.domain;

import javax.persistence.*;

import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profilePicture;

    @Embedded
    private AuditData auditData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id",nullable = false, unique = true)
    private Profile profile;

    @OneToMany(mappedBy = "admin")
    private Set<Competition> createdCompetitions;
}
