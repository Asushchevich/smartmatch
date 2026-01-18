package com.smartmatch.match_service.model;

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
    @Column(nullable = false)
    private String title;

    @Column(name = "start_time")
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private Integer homeTeamScore;
    private Integer awayTeamScore;
}


