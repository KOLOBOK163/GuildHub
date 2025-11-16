package com.guildhub.Service;

import com.guildhub.Dto.TeamDto;
import com.guildhub.Entity.Team;
import com.guildhub.Mapper.TeamMapper;
import com.guildhub.Reposirtory.TeamRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    @Transactional
    public TeamDto createTeam(TeamDto teamDto) {
        if (teamRepository.existsByName(teamDto.getName())) {
            throw new EntityExistsException("Team with name " + teamDto.getName() + " already exists");
        }

        Team team = teamMapper.teamDtoToTeam(teamDto);
        if (team.getPrizes() == null) {
            team.setPrizes(0);
        }
        
        return teamMapper.teamToTeamDto(teamRepository.save(team));
    }

    @Override
    @Transactional
    public TeamDto updateTeam(Long teamId, TeamDto teamDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        if (!team.getName().equals(teamDto.getName()) && 
            teamRepository.existsByName(teamDto.getName())) {
            throw new EntityExistsException("Team name already taken");
        }

        team.setName(teamDto.getName());
        team.setLogoUrl(teamDto.getLogoUrl());
        team.setPrizes(teamDto.getPrizes());
        team.setHltvRating(teamDto.getHltvRating());

        return teamMapper.teamToTeamDto(teamRepository.save(team));
    }

    @Override
    public TeamDto getTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));
        return teamMapper.teamToTeamDto(team);
    }

    @Override
    public List<TeamDto> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::teamToTeamDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTeam(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new EntityNotFoundException("Team not found with id: " + teamId);
        }
        teamRepository.deleteById(teamId);
    }
}


