package eg.mos.sportify.domain;

import eg.mos.sportify.domain.enums.Gender;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a user's profile in the system.

 * This entity contains personal information about the user, including
 * their name, date of birth, location, bio, phone number, and gender.
 */
@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    /**
     * Unique identifier for the profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    /**
     * The first name of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * The last name of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * The date of birth of the user.
     * Stored as a String; consider using a date type for better validation and formatting.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String dateOfBirth;

    /**
     * The location of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String location;

    /**
     * A brief biography of the user.
     */
    private String bio;

    /**
     * The phone number of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String phone;

    /**
     * The gender of the user.
     * It can be one of the values defined in {@link Gender}.
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * Audit data associated with the profile,
     * capturing creation and update timestamps.
     */
    @Embedded
    private AuditData auditData;
}
