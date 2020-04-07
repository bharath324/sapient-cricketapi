package com.sapient.cricketapi.service.impl;

import com.sapient.cricketapi.exceptions.StatIsNotEmptyException;
import com.sapient.cricketapi.model.CricketScore;
import com.sapient.cricketapi.service.CricketScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class CricketScoreServiceImpl implements CricketScoreService {

    Logger logger = LoggerFactory.getLogger(CricketScoreServiceImpl.class);

    public String getWinnerResponse(CricketScore cricketScore) {

        if (cricketScore == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        String stat = cricketScore.getStat();
        String score = cricketScore.getScore();
        String team1 = cricketScore.getTeam1();
        String team2 = cricketScore.getTeam2();

        if (nonNull(stat) && !stat.isEmpty()) {
            throw new StatIsNotEmptyException("\"stat\" in the respose is not empty. " +
                    " Invalid score for unique id. Try different unique id");
        }
        WinnerResult winnerResult = getWinner(score);
        String winnerTeam = winnerResult.getWinner();
        String winnerScore = winnerResult.getScore();
        String roundRotatedScore = winnerResult.getRotatedScore();

        builder.append("Team-1: ").append(team1);
        if (nonNull(winnerTeam) && winnerTeam.equals(team1)) {
            builder.append(" (winner)");
        }
        builder.append("</br>");
        builder.append("Team-2: ").append(team2);
        if (nonNull(winnerTeam) && winnerTeam.equals(team2)) {
            builder.append(" (winner)");
        }
        builder.append("</br>");
        builder.append("Winning team’s score: ");
        builder.append(winnerScore);
        builder.append("</br>");
        builder.append("Round rotation: ");
        builder.append(roundRotatedScore);
        builder.append("</br>");

        return builder.toString();
    }

    private WinnerResult getWinner(String score) {
        WinnerResult winnerResult = null;
        logger.info("Score: " + score);
        if (nonNull(score) && !score.isEmpty()) {
            String regex = "^(\\D+)(\\d+\\/\\d+)\\s+v\\s+(\\D+)(\\d+\\/\\d+)\\s+\\*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(score);
            if (matcher.find()) {
                logger.info("Score pattern found: ");
                String team1 = matcher.group(1);
                logger.info("Group 1: " + team1);
                String scoreOfTeam1 = matcher.group(2);
                logger.info("Group 2: " + scoreOfTeam1);
                String team2 = matcher.group(3);
                logger.info("Group 3: " + team2);
                String scoreOfTeam2 = matcher.group(4);
                logger.info("Group 4: " + scoreOfTeam2);

                String[] scoreTokensForTeam1 = scoreOfTeam1.trim().split("/");
                int team1Runs = Integer.parseInt(scoreTokensForTeam1[0]);

                String[] scoreTokensForTeam2 = scoreOfTeam1.trim().split("/");
                int team2Runs = Integer.parseInt(scoreTokensForTeam2[0]);

                if (team1Runs > team2Runs) {
                    winnerResult = new WinnerResult(team1.trim(), scoreOfTeam1.trim());
                } else {
                    winnerResult = new WinnerResult(team2.trim(), scoreOfTeam2.trim());
                }
            }
        }
        return winnerResult;
    }

    static class WinnerResult {
        String winner;
        String score;
        String rotatedScore;

        public WinnerResult(String winner, String score) {
            this.winner = winner;
            this.score = score;
            this.rotatedScore = rotate(score);
        }

        private String rotate(String score) {
            if (isNull(score) || score.isEmpty()) {
                return null;
            }
            char lastChar = score.charAt(score.length() - 1);
            String rotated = lastChar + score.substring(0, score.length() - 1);
            return rotated;
        }

        public String getWinner() {
            return winner;
        }

        public String getScore() {
            return score;
        }

        public String getRotatedScore() {
            return rotatedScore;
        }
    }
}
