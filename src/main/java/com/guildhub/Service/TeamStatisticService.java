package com.guildhub.Service;

import com.guildhub.Dto.TeamStatisticDto;

import java.time.LocalDate;
import java.util.List;

public interface TeamStatisticService {
    TeamStatisticDto createTeamStatistic(TeamStatisticDto dto);
    TeamStatisticDto updateTeamStatistic(Long statisticId, TeamStatisticDto dto);
    TeamStatisticDto getTeamStatisticById(Long statisticId);
    List<TeamStatisticDto> getAllTeamStatistics();
    List<TeamStatisticDto> getTeamStatisticsByTeamId(Long teamId);
    List<TeamStatisticDto> getTeamStatisticsByTeamIdAndPeriod(Long teamId, LocalDate start, LocalDate end);
    void deleteTeamStatistic(Long statisticId);
}


