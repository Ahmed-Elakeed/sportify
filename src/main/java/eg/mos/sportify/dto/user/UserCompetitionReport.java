package eg.mos.sportify.dto.user;

import eg.mos.sportify.domain.PlayerCompetition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a report of a user's performance in competitions.

 * This class includes the overall winning ratio of the user across all competitions and a list
 * of individual competition records.
 */
@Data
@NoArgsConstructor
public class UserCompetitionReport {

    /**
     * The overall winning ratio of the user across all competitions, represented as a percentage.
     */
    private String overallWinningRatio;

    /**
     * A list of records representing the user's performance in individual competitions.
     */
    private List<UserCompetitionRecord> userCompetitionRecordList;

    /**
     * Constructs the competition report based on the provided list of player competitions.
     *
     * @param playerCompetitionList the list of {@link PlayerCompetition} objects representing
     *                              the user's participation in various competitions.
     */
    public void constructReport(List<PlayerCompetition> playerCompetitionList) {
        this.userCompetitionRecordList = new ArrayList<>();
        this.calculateOverallWinningRatio(playerCompetitionList);
        this.calculateUserCompetitionRecordList(playerCompetitionList);
    }

    /**
     * Calculates the overall winning ratio of the user based on their scores in all competitions.
     *
     * @param playerCompetitionList the list of {@link PlayerCompetition} objects used to calculate the ratio.
     */
    private void calculateOverallWinningRatio(List<PlayerCompetition> playerCompetitionList) {
        Integer totalScore = 0;
        Integer totalMaxScore = 0;
        for (PlayerCompetition playerCompetition : playerCompetitionList) {
            totalScore += playerCompetition.getScore();
            totalMaxScore += playerCompetition.getCompetition().getMaxScore();
        }
        this.overallWinningRatio = String.format("%.2f", ((double) totalScore / totalMaxScore) * 100) + " %";
    }

    /**
     * Populates the user's competition record list based on the player's competition data.
     *
     * @param playerCompetitionList the list of {@link PlayerCompetition} objects to populate the report.
     */
    private void calculateUserCompetitionRecordList(List<PlayerCompetition> playerCompetitionList) {
        playerCompetitionList.forEach(playerCompetition -> this.userCompetitionRecordList.add(
                UserCompetitionRecord.builder()
                        .competitionName(playerCompetition.getCompetition().getName())
                        .startDate(playerCompetition.getCompetition().getStartDate())
                        .endDate(playerCompetition.getCompetition().getEndDate())
                        .maxScore(playerCompetition.getCompetition().getMaxScore())
                        .status(playerCompetition.getCompetition().getStatus())
                        .playerScore(playerCompetition.getScore())
                        .winningRatio(String.format("%.2f",
                                ((double) playerCompetition.getScore() / playerCompetition.getCompetition().getMaxScore()) * 100) + " %"
                        )
                        .build()
        ));
    }
}
