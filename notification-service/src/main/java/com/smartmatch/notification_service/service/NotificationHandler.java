package com.smartmatch.notification_service.service;

import com.smartmatch.common.dto.MatchEvent;
import com.smartmatch.notification_service.model.Notification;
import com.smartmatch.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationHandler {

    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "notification.queue")
    public void handleNotification(MatchEvent event) {
        System.out.println("--------------------------------------------------");
        System.out.println("🔔 УВЕДОМЛЕНИЕ (DTO):");
        System.out.println("📍 Матч ID: " + event.getMatchId());
        System.out.println("⚽ Игра: " + event.getTitle());
        System.out.println("📊 Статус: " + event.getStatus());
        System.out.println("💬 Текст: " + event.getMessage());
        System.out.println("--------------------------------------------------");

        Notification notification = Notification.builder()
                .matchId(event.getMatchId())
                .title(event.getTitle())
                .status(event.getStatus())
                .message(event.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        System.out.println("✅ История уведомления сохранена в БД");
    }
}