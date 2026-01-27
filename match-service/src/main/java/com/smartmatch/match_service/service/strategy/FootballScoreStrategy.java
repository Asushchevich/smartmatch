package com.smartmatch.match_service.service.strategy;

import com.smartmatch.match_service.model.Match;
import org.springframework.stereotype.Component;

@Component("FOOTBALL")
public class FootballScoreStrategy implements ScoreStrategy {

    @Override
    public void updateScore(Match match, String teamSide) {
        if (match.getHomeTeamScore() == null) match.setHomeTeamScore(0);
        if (match.getAwayTeamScore() == null) match.setAwayTeamScore(0);

        if ("HOME".equalsIgnoreCase(teamSide)) {
            match.setHomeTeamScore(match.getHomeTeamScore() + 1);
        } else if ("AWAY".equalsIgnoreCase(teamSide)) {
            match.setAwayTeamScore(match.getAwayTeamScore() + 1);
        }
    }

    @Override
    public void decrementScore(Match match, String teamSide) {
        if ("HOME".equalsIgnoreCase(teamSide) && match.getHomeTeamScore() > 0) {
            match.setHomeTeamScore(match.getHomeTeamScore() - 1);
        } else if ("AWAY".equalsIgnoreCase(teamSide) && match.getAwayTeamScore() > 0) {
            match.setAwayTeamScore(match.getAwayTeamScore() - 1);
        }
    }
}
