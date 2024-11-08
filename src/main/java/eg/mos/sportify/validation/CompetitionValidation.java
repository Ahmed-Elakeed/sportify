package eg.mos.sportify.validation;

import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.security.AuthUserDetailsService;

import java.util.Objects;

public class CompetitionValidation {


    private CompetitionValidation() {}
    /**
     * Validates that the authenticated user is the admin of the competition.
     *
     * @param competition the competition to validate.
     * @throws AuthorizationException if the authenticated user is not the admin of the competition.
     */
    public static void validateCompetitionAdminAuthorization(Competition competition) {
        if (!Objects.equals(competition.getAdmin().getUsername(), AuthUserDetailsService.getUsernameFromToken())) {
            throw new AuthorizationException("Only the admin of the competition can change the status.");
        }
    }

}
