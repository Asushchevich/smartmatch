package com.smartmatch.streaming_service.listener;

import com.smartmatch.common.dto.MatchEvent;
import com.smartmatch.streaming_service.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StreamEventListener {

    private final StreamService streamService;

    @RabbitListener(queues = "stream.queue")
    public void handleMatchEvent(MatchEvent event) {
        log.info(" [x] Получено событие RabbitMQ для матча: {} (Статус: {})",
                event.getMatchId(), event.getStatus());

        if ("FINISHED".equals(event.getStatus()) || "CANCELLED".equals(event.getStatus())) {
            streamService.deactivateStreamsForMatch(event.getMatchId());
            log.info(" [v] Стримы для матча {} успешно деактивированы.", event.getMatchId());
        }
    }
}