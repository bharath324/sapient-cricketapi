package com.sapient.cricketapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.GetMapping;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CricketScore {
    private String stat;
    private String score;
    private String description;
    private boolean matchStarted;
    private String error;

    @JsonProperty("team-1")
    private String team1;

    @JsonProperty("team-2")
    private String team2;

    public CricketScore() {
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMatchStarted() {
        return matchStarted;
    }

    public void setMatchStarted(boolean matchStarted) {
        this.matchStarted = matchStarted;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "CricketScore{" +
                "  stat='" + stat + '\'' +
                ", score=" + score +
                ", team1 = " + team1 +
                ", team2 = " + team2 +
                ", matchstarted = " + matchStarted +
                " }";
    }
}
