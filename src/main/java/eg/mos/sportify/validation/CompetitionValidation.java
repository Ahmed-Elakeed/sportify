package eg.mos.sportify.validation;

import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.User;
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

    /**
     * Validates that the authenticated user is allowed to add a competition.
     *
     * @param user the user to validate.
     * @throws AuthorizationException if the authenticated user is not the same as the user attempting to add the competition.
     */
    public static void validateUserAuthorization(User user) {
        String currentAuthenticatedUsername = AuthUserDetailsService.getUsernameFromToken();
        if (!Objects.equals(currentAuthenticatedUsername, user.getUsername())) {
            throw new AuthorizationException("User can only add competitions for themselves.");
        }
    }
}
