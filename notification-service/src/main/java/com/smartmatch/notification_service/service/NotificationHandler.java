package com.smartmatch.notification_service.service;

import com.smartmatch.common.dto.MatchEvent;
import com.smartmatch.notification_service.model.Notification;
import com.smartmatch.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationHandler {

    private final NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "notification.queue")
    public void handleNotification(MatchEvent event) {
        System.out.println("📩 Получено событие от RabbitMQ: " + event.getAction());

        if ("DELETE".equals(event.getAction())) {
            System.out.println("🗑️ Обработка удаления матча ID: " + event.getMatchId());
        }

        Notification notification = Notification.builder()
                .matchId(event.getMatchId())
                .title(event.getTitle())
                .status(event.getStatus())
                .message(event.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/match-updates", event);

        System.out.println("🚀 Событие [" + event.getAction() + "] отправлено в WebSocket");
    }
}