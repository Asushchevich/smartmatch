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
        if (match.getStatus() == null) {
            match.setStatus(com.smartmatch.match_service.model.MatchStatus.SCHEDULED);
        }

        if (match.getTitle() == null || match.getTitle().isBlank()) {
            String home = (match.getHomeTeam() != null) ? match.getHomeTeam() : "TBD";
            String away = (match.getAwayTeam() != null) ? match.getAwayTeam() : "TBD";
            match.setTitle(home + " — " + away);
        }

        Match savedMatch = matchRepository.save(match);

        MatchEvent event = MatchEvent.builder()
                .matchId(savedMatch.getId())
                .title(savedMatch.getTitle())
                .status(savedMatch.getStatus().toString())
                .message("Матч успешно запланирован")
                .build();

        try {
            rabbitTemplate.convertAndSend(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.ROUTING_KEY,
                    event
            );
            System.out.println(" [x] Sent to RabbitMQ: " + event.getTitle());
        } catch (Exception e) {
            System.err.println(" [!] Failed to send message to RabbitMQ: " + e.getMessage());
        }

        return savedMatch;
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return matchRepository.existsById(id);
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

    @Transactional
    public Match updateMatchStatus(UUID id, com.smartmatch.match_service.model.MatchStatus newStatus) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        match.setStatus(newStatus);
        Match savedMatch = matchRepository.save(match);

        String messageText = switch (newStatus) {
            case SCHEDULED -> "Матч успешно запланирован";
            case LIVE -> "Матч начался! Трансляции доступны.";
            case FINISHED -> "Матч завершен. Спасибо, что были с нами!";
            case CANCELLED -> "Матч отменен.";
            default -> "Статус матча обновлен на: " + newStatus;
        };

        MatchEvent event = MatchEvent.builder()
                .matchId(savedMatch.getId())
                .title(savedMatch.getTitle())
                .status(savedMatch.getStatus().toString())
                .message(messageText)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );

        return savedMatch;
    }
}
