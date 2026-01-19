package com.smartmatch.match_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartmatch.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match extends BaseEntity {
    @Column(nullable = false, name = "title")
    private String title;

    @Column(name = "start_time")
    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(name = "home_team_score")
    @JsonProperty("home_team_score")
    private Integer homeTeamScore;

    @Column(name = "away_team_score")
    @JsonProperty("away_team_score")
    private Integer awayTeamScore;
}


