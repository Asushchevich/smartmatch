package com.smartmatch.match_service.service;

import com.smartmatch.common.dto.MatchEvent;
import com.smartmatch.match_service.config.RabbitConfig;
import com.smartmatch.match_service.model.Match;
import com.smartmatch.match_service.repository.MatchRepository;
import com.smartmatch.match_service.service.strategy.ScoreStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final RabbitTemplate rabbitTemplate;

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
        Match savedMatch = matchRepository.save(match);

        MatchEvent event = MatchEvent.builder()
                .matchId(savedMatch.getId())
                .title(savedMatch.getTitle())
                .status(savedMatch.getStatus() != null ? savedMatch.getStatus().toString() : "SCHEDULED")
                .message("Match has been successfully created!")
                .build();

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);

        return savedMatch;
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
