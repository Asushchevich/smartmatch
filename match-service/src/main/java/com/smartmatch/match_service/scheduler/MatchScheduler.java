package com.smartmatch.match_service.scheduler;

import com.smartmatch.common.dto.MatchEvent;
import com.smartmatch.common.model.SportType;
import com.smartmatch.match_service.model.Match;
import com.smartmatch.match_service.model.MatchStatus;
import com.smartmatch.match_service.repository.MatchRepository;
import com.smartmatch.match_service.service.MatchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchScheduler {

    private final MatchRepository matchRepository;
    private final MatchService matchService;

    @Scheduled(fixedDelay = 60000)
    public void autoFinishMatches() {
        LocalDateTime footballThreshold = LocalDateTime.now().minusMinutes(95);
        List<Match> footballMatches = matchRepository.findAllByStatusAndSportTypeAndStartDateBefore(
                MatchStatus.LIVE,
                SportType.FOOTBALL,
                footballThreshold
        );
        processClosing(footballMatches);

        LocalDateTime basketballThreshold = LocalDateTime.now().minusMinutes(120);
        List<Match> basketballMatches = matchRepository.findAllByStatusAndSportTypeAndStartDateBefore(
                MatchStatus.LIVE,
                SportType.BASKETBALL,
                basketballThreshold
        );
        processClosing(basketballMatches);
    }

    private void processClosing(List<Match> matches) {
        for (Match match : matches) {
            match.setStatus(MatchStatus.FINISHED);
            matchRepository.save(match);
            matchService.sendRabbitEvent(match, "Матч завершен автоматически");
        }
    }
}