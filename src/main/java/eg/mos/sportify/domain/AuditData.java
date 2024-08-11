package eg.mos.sportify.domain;


import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditData {
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
