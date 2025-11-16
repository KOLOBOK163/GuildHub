package com.guildhub.Service;

import com.guildhub.Dto.MatchResultDto;
import com.guildhub.Entity.MatchResult;
import com.guildhub.Entity.Team;
import com.guildhub.Mapper.MatchResultMapper;
import com.guildhub.Reposirtory.MatchResultRepository;
import com.guildhub.Reposirtory.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchResultServiceImpl implements MatchResultService {

    private final MatchResultRepository matchResultRepository;
    private final TeamRepository teamRepository;
    private final MatchResultMapper matchResultMapper;

    public MatchResultServiceImpl(
            MatchResultRepository matchResultRepository,
            TeamRepository teamRepository,
            MatchResultMapper matchResultMapper) {
        this.matchResultRepository = matchResultRepository;
        this.teamRepository = teamRepository;
        this.matchResultMapper = matchResultMapper;
    }

    @Override
    @Transactional
    public MatchResultDto createMatchResult(MatchResultDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));

        MatchResult matchResult = matchResultMapper.matchResultDtoToMatchResult(dto);
        matchResult.setTeam(team);

        if (dto.getOpponentTeamId() != null) {
            Team opponentTeam = teamRepository.findById(dto.getOpponentTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Opponent team not found with id: " + dto.getOpponentTeamId()));
            matchResult.setOpponentTeam(opponentTeam);
        }

        // Determine result based on scores
        if (dto.getTeamScore() != null && dto.getOpponentScore() != null) {
            if (dto.getTeamScore() > dto.getOpponentScore()) {
                matchResult.setResult("WIN");
            } else if (dto.getTeamScore() < dto.getOpponentScore()) {
                matchResult.setResult("LOSS");
            } else {
                matchResult.setResult("DRAW");
            }
        }

        return matchResultMapper.matchResultToMatchResultDto(matchResultRepository.save(matchResult));
    }

    @Override
    @Transactional
    public MatchResultDto updateMatchResult(Long matchResultId, MatchResultDto dto) {
        MatchResult matchResult = matchResultRepository.findById(matchResultId)
                .orElseThrow(() -> new EntityNotFoundException("Match result not found with id: " + matchResultId));

        if (dto.getTeamId() != null && !matchResult.getTeam().getId().equals(dto.getTeamId())) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));
            matchResult.setTeam(team);
        }

        if (dto.getOpponentTeamId() != null) {
            if (matchResult.getOpponentTeam() == null || 
                !matchResult.getOpponentTeam().getId().equals(dto.getOpponentTeamId())) {
                Team opponentTeam = teamRepository.findById(dto.getOpponentTeamId())
                        .orElseThrow(() -> new EntityNotFoundException("Opponent team not found with id: " + dto.getOpponentTeamId()));
                matchResult.setOpponentTeam(opponentTeam);
            }
        }

        matchResult.setOpponentName(dto.getOpponentName());
        matchResult.setMatchDate(dto.getMatchDate());
        matchResult.setMap(dto.getMap());
        matchResult.setTeamScore(dto.getTeamScore());
        matchResult.setOpponentScore(dto.getOpponentScore());
        matchResult.setTournament(dto.getTournament());
        matchResult.setMatchType(dto.getMatchType());
        matchResult.setDescription(dto.getDescription());

        // Update result based on scores
        if (dto.getTeamScore() != null && dto.getOpponentScore() != null) {
            if (dto.getTeamScore() > dto.getOpponentScore()) {
                matchResult.setResult("WIN");
            } else if (dto.getTeamScore() < dto.getOpponentScore()) {
                matchResult.setResult("LOSS");
            } else {
                matchResult.setResult("DRAW");
            }
        }

        return matchResultMapper.matchResultToMatchResultDto(matchResultRepository.save(matchResult));
    }

    @Override
    public MatchResultDto getMatchResultById(Long matchResultId) {
        MatchResult matchResult = matchResultRepository.findById(matchResultId)
                .orElseThrow(() -> new EntityNotFoundException("Match result not found with id: " + matchResultId));
        return matchResultMapper.matchResultToMatchResultDto(matchResult);
    }

    @Override
    public List<MatchResultDto> getAllMatchResults() {
        return matchResultRepository.findAll().stream()
                .map(matchResultMapper::matchResultToMatchResultDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchResultDto> getMatchResultsByTeamId(Long teamId) {
        return matchResultRepository.findByTeamIdOrderByMatchDateDesc(teamId).stream()
                .map(matchResultMapper::matchResultToMatchResultDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchResultDto> getMatchResultsByResult(String result) {
        return matchResultRepository.findByResult(result).stream()
                .map(matchResultMapper::matchResultToMatchResultDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMatchResult(Long matchResultId) {
        if (!matchResultRepository.existsById(matchResultId)) {
            throw new EntityNotFoundException("Match result not found with id: " + matchResultId);
        }
        matchResultRepository.deleteById(matchResultId);
    }
}


