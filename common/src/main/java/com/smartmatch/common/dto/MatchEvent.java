package com.smartmatch.common.dto;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchEvent {
    private UUID matchId;
    private String title;
    private String status;
    private String message;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
}