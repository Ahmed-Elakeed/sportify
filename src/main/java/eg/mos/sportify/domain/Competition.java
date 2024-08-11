package eg.mos.sportify.domain;


import eg.mos.sportify.domain.enums.CompetitionStatus;
import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;


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

    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private CompetitionStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User admin;

    @OneToMany(mappedBy = "competition")
    private Set<PlayerCompetition> playerCompetitions;

    @Embedded
    private AuditData auditData;
}
