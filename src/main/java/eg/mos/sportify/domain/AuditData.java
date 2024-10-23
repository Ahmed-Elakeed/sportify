package eg.mos.sportify.domain;


import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Represents audit data for an entity, capturing creation and update timestamps.

 * This class is embeddable and can be included in other entity classes
 * to track when an entity was created and last updated.
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditData {
    /**
     * The timestamp when the entity was created.
     * Automatically set to the current date and time upon creation.
     */
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * The timestamp when the entity was last updated.
     * This field should be updated to the current date and time whenever
     * the entity is modified.
     */
    private LocalDateTime updatedAt;
}
