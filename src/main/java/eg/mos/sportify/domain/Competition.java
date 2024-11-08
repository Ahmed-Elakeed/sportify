package eg.mos.sportify.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;



/**
 * Represents a competition in the system.

 * This entity contains details about the competition, including its name,
 * description, dates, maximum score, status, and associated admin.
 */
@Entity
@Table(name = "competitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competition {

    /**
     * Unique identifier for the competition.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long competitionId;

    /**
     * Name of the competition.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Description of the competition.
     */
    private String description;

    /**
     * The start date and time of the competition.
     * Cannot be null.
     */
    @Column(nullable = false)
    private LocalDateTime startDate;

    /**
     * The end date and time of the competition.
     * Cannot be null.
     */
    @Column(nullable = false)
    private LocalDateTime endDate;

    /**
     * The maximum score a participant can achieve in the competition.
     * Cannot be null.
     */
    @Column(nullable = false)
    private Integer maxScore;

    /**
     * The current status of the competition.
     * It can be one of the values defined in {@link CompetitionStatus}.
     */
    @Enumerated(EnumType.STRING)
    private CompetitionStatus status;

    /**
     * The admin user responsible for managing the competition.
     * This relationship is many-to-one; each competition has one admin.
     * This field is ignored during JSON serialization to prevent circular references.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User admin;

    /**
     * Audit data associated with the competition,
     * capturing creation and update timestamps.
     */
    @Embedded
    private AuditData auditData;
}
