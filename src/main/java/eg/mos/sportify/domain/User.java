package eg.mos.sportify.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eg.mos.sportify.dto.user.UserRegistrationDTO;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;


/**
 * Represents a user in the system.

 * This entity contains user credentials, profile information, and associations
 * with competitions created by the user.
 */
@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * The username of the user.
     * Must be unique and cannot be null.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * The email address of the user.
     * Must be unique and cannot be null.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The password of the user.
     * Stored as a hashed value and is ignored during JSON serialization.
     * Cannot be null.
     */
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    /**
     * The profile picture of the user.
     * Can be null.
     */
    private String profilePicture;

    /**
     * Audit data associated with the user,
     * capturing creation and update timestamps.
     */
    @Embedded
    private AuditData auditData;

    /**
     * The profile associated with the user.
     * Each user has a one-to-one relationship with their profile.
     * This relationship cascades all operations, ensuring changes to the user
     * are reflected in the profile.
     * The profile is required and unique to each user.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id",nullable = false, unique = true)
    private Profile profile;

    /**
     * The set of competitions created by the user.
     * This is a one-to-many relationship; a user can create multiple competitions.
     */
    @OneToMany(mappedBy = "admin")
    private Set<Competition> createdCompetitions;


    public static User createUser(UserRegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(userRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .email(userRegistrationDTO.getEmail())
                .profilePicture(userRegistrationDTO.getProfilePicture())
                .profile(Profile.createProfile(userRegistrationDTO))
                .auditData(AuditData.createAuditData())
                .build();
    }
}
