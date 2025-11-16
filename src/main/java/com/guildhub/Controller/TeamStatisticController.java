package com.guildhub.Controller;

import com.guildhub.Dto.TeamStatisticDto;
import com.guildhub.Service.TeamStatisticService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/team-statistics")
public class TeamStatisticController {

    private final TeamStatisticService teamStatisticService;

    public TeamStatisticController(TeamStatisticService teamStatisticService) {
        this.teamStatisticService = teamStatisticService;
    }

    @PostMapping
    public ResponseEntity<TeamStatisticDto> createTeamStatistic(@Valid @RequestBody TeamStatisticDto dto) {
        return ResponseEntity.ok(teamStatisticService.createTeamStatistic(dto));
    }

    @PutMapping("/{statisticId}")
    public ResponseEntity<TeamStatisticDto> updateTeamStatistic(
            @PathVariable Long statisticId,
            @Valid @RequestBody TeamStatisticDto dto) {
        return ResponseEntity.ok(teamStatisticService.updateTeamStatistic(statisticId, dto));
    }

    @GetMapping("/{statisticId}")
    public ResponseEntity<TeamStatisticDto> getTeamStatisticById(@PathVariable Long statisticId) {
        return ResponseEntity.ok(teamStatisticService.getTeamStatisticById(statisticId));
    }

    @GetMapping
    public ResponseEntity<List<TeamStatisticDto>> getAllTeamStatistics() {
        return ResponseEntity.ok(teamStatisticService.getAllTeamStatistics());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamStatisticDto>> getTeamStatisticsByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamStatisticService.getTeamStatisticsByTeamId(teamId));
    }

    @GetMapping("/team/{teamId}/period")
    public ResponseEntity<List<TeamStatisticDto>> getTeamStatisticsByTeamIdAndPeriod(
            @PathVariable Long teamId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(teamStatisticService.getTeamStatisticsByTeamIdAndPeriod(teamId, start, end));
    }

    @DeleteMapping("/{statisticId}")
    public ResponseEntity<Void> deleteTeamStatistic(@PathVariable Long statisticId) {
        teamStatisticService.deleteTeamStatistic(statisticId);
        return ResponseEntity.noContent().build();
    }
}


