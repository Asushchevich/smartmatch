package com.smartmatch.match_service.controller;

import com.smartmatch.match_service.model.Match;
import com.smartmatch.match_service.repository.MatchRepository;
import com.smartmatch.match_service.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody Match match) {
        return ResponseEntity.ok(matchService.createMatch(match));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable UUID id) {
        return matchService.getMatchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
