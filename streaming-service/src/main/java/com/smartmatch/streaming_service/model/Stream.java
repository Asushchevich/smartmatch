package com.smartmatch.streaming_service.model;

import com.smartmatch.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "streams")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Stream extends BaseEntity {

    @Column(name = "match_id", nullable = false)
    private UUID matchId;

    @Column(name = "stream_url", nullable = false)
    private String streamUrl;

    private String provider; // YouTube, Twitch, HLS

    @Column(name = "is_active")
    private boolean active;
}