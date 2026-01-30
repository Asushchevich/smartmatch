package com.smartmatch.match_service.service;

import com.smartmatch.common.dto.MatchEvent;
import com.smartmatch.common.dto.NotificationDTO;
import com.smartmatch.match_service.config.RabbitConfig;
import com.smartmatch.match_service.model.Match;
import com.smartmatch.match_service.repository.MatchRepository;
import com.smartmatch.match_service.service.strategy.ScoreStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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

    @Cacheable(value = "matchesList")
    @Transactional(readOnly = true)
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Match> getMatchById(UUID id) {
        return matchRepository.findById(id);
    }

    @CacheEvict(value = "matchesList", allEntries = true)
    @Transactional
    public Match createMatch(Match match) {
        if (match.getStatus() == null) {
            match.setStatus(com.smartmatch.match_service.model.MatchStatus.SCHEDULED);
        }

        match.setHomeTeamScore(0);
        match.setAwayTeamScore(0);

        if (match.getTitle() == null || match.getTitle().isBlank()) {
            String home = (match.getHomeTeam() != null) ? match.getHomeTeam() : "TBD";
            String away = (match.getAwayTeam() != null) ? match.getAwayTeam() : "TBD";
            match.setTitle(home + " — " + away);
        }

        Match savedMatch = matchRepository.save(match);
        sendRabbitEvent(savedMatch, "Матч успешно запланирован");

        return savedMatch;
    }

    @Transactional
    public void deleteMatch(UUID id) {
        Match match = matchRepository.findById(id).orElseThrow();
        matchRepository.deleteById(id);

        MatchEvent deleteEvent = new MatchEvent();
        deleteEvent.setMatchId(id);
        deleteEvent.setTitle(match.getTitle());
        deleteEvent.setAction("DELETE");
        deleteEvent.setMessage("Матч был удален администратором");

        rabbitTemplate.convertAndSend("notification.queue", deleteEvent);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return matchRepository.existsById(id);
    }

    @CacheEvict(value = "matchesList", allEntries = true)
    @Transactional
    public void goalScored(UUID matchId, String sportType, String teamSide, int points){ // Добавь параметр points
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        ScoreStrategy strategy = strategies.get(sportType.toUpperCase());

        if (strategy != null) {
            strategy.updateScore(match, teamSide, points);
            Match savedMatch = matchRepository.save(match);

            String msg = points > 1 ? "Результативная атака! +" + points : "ГОООЛ!";
            sendRabbitEvent(savedMatch, msg + " Текущий счет " + savedMatch.getHomeTeamScore() + ":" + savedMatch.getAwayTeamScore());
        }
    }

    @CacheEvict(value = "matchesList", allEntries = true)
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

        sendRabbitEvent(savedMatch, messageText);
        return savedMatch;
    }

    @CacheEvict(value = "matchesList", allEntries = true)
    @Transactional
    public void goalCancelled(UUID matchId, String sportType, String teamSide) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        ScoreStrategy strategy = strategies.get(sportType.toUpperCase());
        if (strategy != null) {
            strategy.decrementScore(match, teamSide);
            Match savedMatch = matchRepository.save(match);
            sendRabbitEvent(savedMatch, "Гол отменен! Текущий счет " + savedMatch.getHomeTeamScore() + ":" + savedMatch.getAwayTeamScore());
        }
    }

    public void sendRabbitEvent(Match match, String message) {
        MatchEvent event = MatchEvent.builder()
                .matchId(match.getId())
                .title(match.getTitle())
                .status(match.getStatus().toString())
                .homeTeamScore(match.getHomeTeamScore())
                .awayTeamScore(match.getAwayTeamScore())
                .sportType(match.getSportType())
                .action("UPDATE")
                .message(message)
                .build();

        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
        } catch (Exception e) {
            System.err.println(" [!] Failed to send message to RabbitMQ: " + e.getMessage());
        }
    }
}
