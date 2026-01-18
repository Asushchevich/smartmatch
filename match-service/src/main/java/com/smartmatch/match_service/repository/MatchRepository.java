package com.smartmatch.match_service.repository;

import com.smartmatch.match_service.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchRepository  extends JpaRepository<Match, UUID> {

}
