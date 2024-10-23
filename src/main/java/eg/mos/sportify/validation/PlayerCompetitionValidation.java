package eg.mos.sportify.validation;

import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.PlayerCompetition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.ValidationException;

public class PlayerCompetitionValidation {

    private PlayerCompetitionValidation(){}

    public static void validateCompetitionStatus(Competition competition) {
        if (competition.getStatus() != CompetitionStatus.UPCOMING) {
            throw new AuthorizationException("Only upcoming competitions can have players added.");
        }
    }

    public static void validateScore(int score, int maxScore) {
        if (score > maxScore) {
            throw new ValidationException("Score exceeds the maximum allowed score of " + maxScore);
        }
    }

    public static void validateRemovalPermission(PlayerCompetition playerCompetition, User currentUser) {
        if (!playerCompetition.getPlayer().getUserId().equals(currentUser.getUserId())
                && !playerCompetition.getCompetition().getAdmin().getUserId().equals(currentUser.getUserId())) {
            throw new AuthorizationException("You do not have permission to remove this player.");
        }
    }
}
