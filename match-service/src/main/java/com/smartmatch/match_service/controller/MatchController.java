package com.smartmatch.match_service.controller;

import com.smartmatch.match_service.model.Match;
import com.smartmatch.match_service.model.MatchStatus;
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
    public ResponseEntity<Match> getMatchById(@PathVariable("id") UUID id) {
        return matchService.getMatchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/exists")
    public boolean checkMatchExists(@PathVariable("id") UUID id) {
        return matchService.existsById(id);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Match> updateStatus(
            @PathVariable("id") UUID id,
            @RequestParam("status") MatchStatus status) {
        return ResponseEntity.ok(matchService.updateMatchStatus(id, status));
    }

    @PatchMapping("/{id}/goal")
    public ResponseEntity<Void> updateScore(
            @PathVariable("id") UUID id,
            @RequestParam("sportType") String sportType,
            @RequestParam("teamSide") String teamSide,
            @RequestParam(value = "points", defaultValue = "1") int points) {

        matchService.goalScored(id, sportType, teamSide, points);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/goal-cancel")
    public ResponseEntity<Void> cancelGoal(
            @PathVariable("id") UUID id,
            @RequestParam("sportType") String sportType,
            @RequestParam("teamSide") String teamSide) {
        matchService.goalCancelled(id, sportType, teamSide);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable UUID id) {
        matchService.deleteMatch(id);
        return ResponseEntity.ok().build();
    }
}