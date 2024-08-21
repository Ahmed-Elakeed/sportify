package eg.mos.sportify.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "competitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long competitionId;

    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = false)
    private Integer maxScore;

    @Enumerated(EnumType.STRING)
    private CompetitionStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User admin;

    @Embedded
    private AuditData auditData;
}
