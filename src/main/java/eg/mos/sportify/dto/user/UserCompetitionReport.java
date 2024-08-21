package eg.mos.sportify.dto.user;

import eg.mos.sportify.domain.PlayerCompetition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserCompetitionReport {
    private String overallWinningRatio;
    private List<UserCompetitionRecord> userCompetitionRecordList;


    public void constructReport(List<PlayerCompetition> playerCompetitionList) {
        this.userCompetitionRecordList = new ArrayList<>();
        this.calculateOverallWinningRatio(playerCompetitionList);
        this.calculateUserCompetitionRecordList(playerCompetitionList);
    }

    private void calculateOverallWinningRatio(List<PlayerCompetition> playerCompetitionList) {
        Integer totalScore = 0;
        Integer totalMaxScore = 0;
        for (PlayerCompetition playerCompetition : playerCompetitionList) {
            totalScore += playerCompetition.getScore();
            totalMaxScore += playerCompetition.getCompetition().getMaxScore();
        }
        this.overallWinningRatio = String.format("%.2f", ((double) totalScore / totalMaxScore) * 100) + " %";
    }


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
