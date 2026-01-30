package com.smartmatch.common.dto;

import com.smartmatch.common.model.SportType;
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
    private String action;
    private SportType sportType;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
}