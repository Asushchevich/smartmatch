package com.smartmatch.streaming_service.repository;

import com.smartmatch.streaming_service.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface StreamRepository extends JpaRepository<Stream, UUID> {
    List<Stream> findByMatchId(UUID matchId);
    List<Stream> findByActiveTrue();
}