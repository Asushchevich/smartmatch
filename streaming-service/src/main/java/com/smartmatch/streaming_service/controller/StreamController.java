package com.smartmatch.streaming_service.controller;

import com.smartmatch.streaming_service.dto.StreamRequest;
import com.smartmatch.streaming_service.model.Stream;
import com.smartmatch.streaming_service.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/streams")
@RequiredArgsConstructor
public class StreamController {

    private final StreamService streamService;

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<Stream>> getByMatch(@PathVariable("matchId") UUID matchId) {
        return ResponseEntity.ok(streamService.getStreamsForMatch(matchId));
    }

    @PostMapping
    public ResponseEntity<Stream> addStream(@RequestBody StreamRequest request) {
        return ResponseEntity.ok(streamService.createStream(request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable("id") UUID id,
            @RequestParam("active") boolean active) {
        streamService.toggleStreamStatus(id, active);
        return ResponseEntity.ok().build();
    }
}