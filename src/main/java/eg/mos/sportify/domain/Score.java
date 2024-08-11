package eg.mos.sportify.domain;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scores")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    private Integer score;

    @Embedded
    private AuditData auditData;
}
