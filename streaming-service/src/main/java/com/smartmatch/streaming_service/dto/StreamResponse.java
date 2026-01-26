package com.smartmatch.streaming_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamResponse {
    private UUID id;
    private UUID matchId;
    private String streamUrl;
    private String provider;
    private boolean active;
}