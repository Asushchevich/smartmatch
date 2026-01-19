package com.smartmatch.match_service.service.strategy;

import com.smartmatch.match_service.model.Match;
import org.springframework.stereotype.Component;

@Component("FOOTBALL")
public class FootballScoreStrategy implements ScoreStrategy {
    @Override
    public void updateScore(Match match, String teamSide) {
        if ("HOME".equalsIgnoreCase(teamSide)) {
            match.setHomeTeamScore(match.getHomeTeamScore() + 1);
        }else if ("AWAY".equalsIgnoreCase(teamSide)) {
            match.setAwayTeamScore(match.getAwayTeamScore() + 1);
        }
    }
}
