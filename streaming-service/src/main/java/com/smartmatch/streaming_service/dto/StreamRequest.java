package com.smartmatch.streaming_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record StreamRequest(
        @NotNull(message = "matchId обязателен")
        UUID matchId,

        @NotBlank(message = "URL стрима не может быть пустым")
        String streamUrl,

        String provider
) {}