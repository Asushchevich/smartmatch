package com.smartmatch.match_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartmatch.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column(name = "home_team")
    @JsonProperty("teamA")
    @NotBlank(message = "Название домашней команды не может быть пустым")
    private String homeTeam;

    @Column(name = "away_team")
    @JsonProperty("teamB")
    @NotBlank(message = "Название гостевой команды не может быть пустым")
    private String awayTeam;

    @Column(name = "start_time")
    @JsonProperty("start_date")
    @NotNull(message = "Дата начала обязательна")
    @Future(message = "Матч должен быть в будущем")
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(name = "home_team_score")
    @JsonProperty("home_team_score")
    private Integer homeTeamScore = 0;

    @Column(name = "away_team_score")
    @JsonProperty("away_team_score")
    private Integer awayTeamScore = 0;
}


