package eg.mos.sportify.domain;

import eg.mos.sportify.domain.enums.Gender;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String dateOfBirth;
    @Column(nullable = false)
    private String location;
    private String bio;
    @Column(nullable = false)
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private AuditData auditData;
}
