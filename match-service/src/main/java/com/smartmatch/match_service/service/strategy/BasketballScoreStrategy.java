package com.smartmatch.match_service.service.strategy;

import com.smartmatch.match_service.model.Match;
import org.springframework.stereotype.Component;

@Component("BASKETBALL")
public class BasketballScoreStrategy implements ScoreStrategy {

    @Override
    public void updateScore(Match match, String teamSide, int points) {
        if ("HOME".equalsIgnoreCase(teamSide)) {
            match.setHomeTeamScore(match.getHomeTeamScore() + points);
        } else {
            match.setAwayTeamScore(match.getAwayTeamScore() + points);
        }
    }

    @Override
    public void decrementScore(Match match, String teamSide) {
        // В баскетболе отмена гола сложнее, но для простоты -1 (или храни историю бросков)
        if ("HOME".equalsIgnoreCase(teamSide)) {
            match.setHomeTeamScore(Math.max(0, match.getHomeTeamScore() - 1));
        } else {
            match.setAwayTeamScore(Math.max(0, match.getAwayTeamScore() - 1));
        }
    }
}