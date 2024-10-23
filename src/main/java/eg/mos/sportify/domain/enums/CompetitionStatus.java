package eg.mos.sportify.domain.enums;


/**
 * Enum representing the possible statuses of a competition.

 * This enum defines the different states a competition can be in:
 * - {@link #UPCOMING}: The competition is scheduled to take place in the future.
 * - {@link #ONGOING}: The competition is currently in progress.
 * - {@link #COMPLETED}: The competition has finished.
 * - {@link #CANCELLED}: The competition has been cancelled and will not take place.
 */
public enum CompetitionStatus {
    UPCOMING, ONGOING, COMPLETED, CANCELLED
}
