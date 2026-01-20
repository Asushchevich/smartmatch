package com.smartmatch.notification_service.service;

import com.smartmatch.common.dto.MatchEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationHandler {

    @RabbitListener(queues = "notification.queue")
    public void handleNotification(MatchEvent event) {
        System.out.println("--------------------------------------------------");
        System.out.println("🔔 УВЕДОМЛЕНИЕ (DTO):");
        System.out.println("📍 Матч ID: " + event.getMatchId());
        System.out.println("⚽ Игра: " + event.getTitle());
        System.out.println("📊 Статус: " + event.getStatus());
        System.out.println("💬 Текст: " + event.getMessage());
        System.out.println("--------------------------------------------------");
    }
}