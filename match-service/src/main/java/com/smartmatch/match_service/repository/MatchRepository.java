package com.smartmatch.match_service.repository;

import com.smartmatch.common.model.SportType;
import com.smartmatch.match_service.model.Match;
import com.smartmatch.match_service.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MatchRepository  extends JpaRepository<Match, UUID> {
    List<Match> findAllByStatusAndSportTypeAndStartDateBefore(
            MatchStatus status,
            SportType sportType,
            LocalDateTime dateTime
    );

}
