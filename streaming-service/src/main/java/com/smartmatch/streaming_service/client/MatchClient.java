package com.smartmatch.streaming_service.client;

import com.smartmatch.streaming_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = "match-service", url = "http://localhost:8081", configuration = FeignConfig.class)
public interface MatchClient {
    @GetMapping("/api/v1/matches/{id}/exists")
    boolean checkMatchExists(@PathVariable("id") UUID id);
}