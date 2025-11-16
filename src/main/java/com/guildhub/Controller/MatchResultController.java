package com.guildhub.Controller;

import com.guildhub.Dto.MatchResultDto;
import com.guildhub.Service.MatchResultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match-results")
public class MatchResultController {

    private final MatchResultService matchResultService;

    public MatchResultController(MatchResultService matchResultService) {
        this.matchResultService = matchResultService;
    }

    @PostMapping
    public ResponseEntity<MatchResultDto> createMatchResult(@Valid @RequestBody MatchResultDto dto) {
        return ResponseEntity.ok(matchResultService.createMatchResult(dto));
    }

    @PutMapping("/{matchResultId}")
    public ResponseEntity<MatchResultDto> updateMatchResult(
            @PathVariable Long matchResultId,
            @Valid @RequestBody MatchResultDto dto) {
        return ResponseEntity.ok(matchResultService.updateMatchResult(matchResultId, dto));
    }

    @GetMapping("/{matchResultId}")
    public ResponseEntity<MatchResultDto> getMatchResultById(@PathVariable Long matchResultId) {
        return ResponseEntity.ok(matchResultService.getMatchResultById(matchResultId));
    }

    @GetMapping
    public ResponseEntity<List<MatchResultDto>> getAllMatchResults() {
        return ResponseEntity.ok(matchResultService.getAllMatchResults());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<MatchResultDto>> getMatchResultsByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(matchResultService.getMatchResultsByTeamId(teamId));
    }

    @GetMapping("/result/{result}")
    public ResponseEntity<List<MatchResultDto>> getMatchResultsByResult(@PathVariable String result) {
        return ResponseEntity.ok(matchResultService.getMatchResultsByResult(result));
    }

    @DeleteMapping("/{matchResultId}")
    public ResponseEntity<Void> deleteMatchResult(@PathVariable Long matchResultId) {
        matchResultService.deleteMatchResult(matchResultId);
        return ResponseEntity.noContent().build();
    }
}


