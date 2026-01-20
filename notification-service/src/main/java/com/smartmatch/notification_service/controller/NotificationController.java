package com.smartmatch.notification_service.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamNotifications() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(i -> "Событие #" + i + ": Гол в матче!");
    }
}