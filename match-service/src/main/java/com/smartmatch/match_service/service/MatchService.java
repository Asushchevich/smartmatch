package com.smartmatch.match_service.service;

import com.smartmatch.match_service.model.Match;
import com.smartmatch.match_service.repository.MatchRepository;
import com.smartmatch.match_service.service.strategy.ScoreStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final Map<String, ScoreStrategy> strategies;

    @Transactional(readOnly = true)
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Match> getMatchById(UUID id) {
        return matchRepository.findById(id);
    }

    @Transactional
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    public void goalScored(UUID matchId, String sportType, String teamSide){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        ScoreStrategy strategy = strategies.get(sportType.toUpperCase());

        if (strategy != null) {
            strategy.updateScore(match, teamSide);
            matchRepository.save(match);
        }
    }
}
