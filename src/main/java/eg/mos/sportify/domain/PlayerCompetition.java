package eg.mos.sportify.domain;

import eg.mos.sportify.domain.enums.PlayerRole;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_competitions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerCompetition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerCompetitionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User player;

    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @Enumerated(EnumType.STRING)
    private PlayerRole role;

    @Embedded
    private AuditData auditData;
}
