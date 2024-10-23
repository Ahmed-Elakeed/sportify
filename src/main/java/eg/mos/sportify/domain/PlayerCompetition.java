package eg.mos.sportify.domain;

import eg.mos.sportify.domain.enums.PlayerRole;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * Represents the association between a player and a competition.

 * This entity captures the player's participation in a competition,
 * including their role, score, and associated audit data.
 */
@Entity
@Table(name = "player_competitions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerCompetition {

    /**
     * Unique identifier for the player competition association.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerCompetitionId;

    /**
     * The player participating in the competition.
     * This relationship is many-to-one; each player can participate in multiple competitions.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User player;

    /**
     * The competition in which the player is participating.
     * This relationship is many-to-one; each competition can have multiple players.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    /**
     * The role of the player in the competition.
     * It can be one of the values defined in {@link PlayerRole}.
     */
    @Enumerated(EnumType.STRING)
    private PlayerRole role;

    /**
     * The score achieved by the player in the competition.
     */
    private Integer score;

    /**
     * Audit data associated with the player competition record,
     * capturing creation and update timestamps.
     */
    @Embedded
    private AuditData auditData;
}
