package com.guildhub.Controller;

import com.guildhub.Dto.TeamDto;
import com.guildhub.Service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@Valid @RequestBody TeamDto teamDto) {
        return ResponseEntity.ok(teamService.createTeam(teamDto));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamDto> updateTeam(
            @PathVariable Long teamId,
            @Valid @RequestBody TeamDto teamDto) {
        return ResponseEntity.ok(teamService.updateTeam(teamId, teamDto));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDto> getTeamById(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}


