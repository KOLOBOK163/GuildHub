package com.guildhub.Service;

import com.guildhub.Dto.TeamStatisticDto;
import com.guildhub.Entity.Team;
import com.guildhub.Entity.TeamStatistic;
import com.guildhub.Mapper.TeamStatisticMapper;
import com.guildhub.Reposirtory.TeamRepository;
import com.guildhub.Reposirtory.TeamStatisticRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamStatisticServiceImpl implements TeamStatisticService {

    private final TeamStatisticRepository teamStatisticRepository;
    private final TeamRepository teamRepository;
    private final TeamStatisticMapper teamStatisticMapper;

    public TeamStatisticServiceImpl(
            TeamStatisticRepository teamStatisticRepository,
            TeamRepository teamRepository,
            TeamStatisticMapper teamStatisticMapper) {
        this.teamStatisticRepository = teamStatisticRepository;
        this.teamRepository = teamRepository;
        this.teamStatisticMapper = teamStatisticMapper;
    }

    @Override
    @Transactional
    public TeamStatisticDto createTeamStatistic(TeamStatisticDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));

        TeamStatistic statistic = teamStatisticMapper.teamStatisticDtoToTeamStatistic(dto);
        statistic.setTeam(team);

        // Calculate win rate if matches data is available
        if (statistic.getMatchesPlayed() != null && statistic.getMatchesPlayed() > 0) {
            double winRate = (double) statistic.getMatchesWon() / statistic.getMatchesPlayed() * 100;
            statistic.setWinRate(winRate);
        }

        // Calculate round win rate if rounds data is available
        if (statistic.getTotalRounds() != null && statistic.getTotalRounds() > 0) {
            double roundWinRate = (double) statistic.getRoundsWon() / statistic.getTotalRounds() * 100;
            statistic.setRoundWinRate(roundWinRate);
        }

        // Calculate average KD if kills and deaths are available
        if (statistic.getTotalDeaths() != null && statistic.getTotalDeaths() > 0 && 
            statistic.getTotalKills() != null) {
            double avgKD = (double) statistic.getTotalKills() / statistic.getTotalDeaths();
            statistic.setAverageKD(avgKD);
        }

        return teamStatisticMapper.teamStatisticToTeamStatisticDto(teamStatisticRepository.save(statistic));
    }

    @Override
    @Transactional
    public TeamStatisticDto updateTeamStatistic(Long statisticId, TeamStatisticDto dto) {
        TeamStatistic statistic = teamStatisticRepository.findById(statisticId)
                .orElseThrow(() -> new EntityNotFoundException("Team statistic not found with id: " + statisticId));

        if (dto.getTeamId() != null && !statistic.getTeam().getId().equals(dto.getTeamId())) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));
            statistic.setTeam(team);
        }

        statistic.setPeriodStart(dto.getPeriodStart());
        statistic.setPeriodEnd(dto.getPeriodEnd());
        statistic.setMatchesPlayed(dto.getMatchesPlayed());
        statistic.setMatchesWon(dto.getMatchesWon());
        statistic.setMatchesLost(dto.getMatchesLost());
        statistic.setAverageRating(dto.getAverageRating());
        statistic.setTotalRounds(dto.getTotalRounds());
        statistic.setRoundsWon(dto.getRoundsWon());
        statistic.setRoundsLost(dto.getRoundsLost());
        statistic.setMostPlayedMap(dto.getMostPlayedMap());
        statistic.setBestMap(dto.getBestMap());
        statistic.setWorstMap(dto.getWorstMap());
        statistic.setTotalKills(dto.getTotalKills());
        statistic.setTotalDeaths(dto.getTotalDeaths());
        statistic.setTotalAssists(dto.getTotalAssists());

        // Recalculate rates
        if (statistic.getMatchesPlayed() != null && statistic.getMatchesPlayed() > 0) {
            double winRate = (double) statistic.getMatchesWon() / statistic.getMatchesPlayed() * 100;
            statistic.setWinRate(winRate);
        }

        if (statistic.getTotalRounds() != null && statistic.getTotalRounds() > 0) {
            double roundWinRate = (double) statistic.getRoundsWon() / statistic.getTotalRounds() * 100;
            statistic.setRoundWinRate(roundWinRate);
        }

        if (statistic.getTotalDeaths() != null && statistic.getTotalDeaths() > 0 && 
            statistic.getTotalKills() != null) {
            double avgKD = (double) statistic.getTotalKills() / statistic.getTotalDeaths();
            statistic.setAverageKD(avgKD);
        }

        return teamStatisticMapper.teamStatisticToTeamStatisticDto(teamStatisticRepository.save(statistic));
    }

    @Override
    public TeamStatisticDto getTeamStatisticById(Long statisticId) {
        TeamStatistic statistic = teamStatisticRepository.findById(statisticId)
                .orElseThrow(() -> new EntityNotFoundException("Team statistic not found with id: " + statisticId));
        return teamStatisticMapper.teamStatisticToTeamStatisticDto(statistic);
    }

    @Override
    public List<TeamStatisticDto> getAllTeamStatistics() {
        return teamStatisticRepository.findAll().stream()
                .map(teamStatisticMapper::teamStatisticToTeamStatisticDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamStatisticDto> getTeamStatisticsByTeamId(Long teamId) {
        return teamStatisticRepository.findByTeamIdOrderByPeriodEndDesc(teamId).stream()
                .map(teamStatisticMapper::teamStatisticToTeamStatisticDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamStatisticDto> getTeamStatisticsByTeamIdAndPeriod(Long teamId, LocalDate start, LocalDate end) {
        return teamStatisticRepository.findByTeamIdAndPeriodStartBetween(teamId, start, end).stream()
                .map(teamStatisticMapper::teamStatisticToTeamStatisticDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTeamStatistic(Long statisticId) {
        if (!teamStatisticRepository.existsById(statisticId)) {
            throw new EntityNotFoundException("Team statistic not found with id: " + statisticId);
        }
        teamStatisticRepository.deleteById(statisticId);
    }
}


