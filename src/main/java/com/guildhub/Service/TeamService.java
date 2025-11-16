package com.guildhub.Service;

import com.guildhub.Dto.TeamDto;

import java.util.List;

public interface TeamService {
    TeamDto createTeam(TeamDto teamDto);
    TeamDto updateTeam(Long teamId, TeamDto teamDto);
    TeamDto getTeamById(Long teamId);
    List<TeamDto> getAllTeams();
    void deleteTeam(Long teamId);
}


