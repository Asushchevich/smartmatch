package com.smartmatch.match_service.service.strategy;

import com.smartmatch.match_service.model.Match;

public interface ScoreStrategy {
        void updateScore(Match match, String teamSide, int points);
        void decrementScore(Match match, String teamSide);
}