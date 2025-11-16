package com.guildhub.Service;

import com.guildhub.Dto.MatchResultDto;

import java.util.List;

public interface MatchResultService {
    MatchResultDto createMatchResult(MatchResultDto dto);
    MatchResultDto updateMatchResult(Long matchResultId, MatchResultDto dto);
    MatchResultDto getMatchResultById(Long matchResultId);
    List<MatchResultDto> getAllMatchResults();
    List<MatchResultDto> getMatchResultsByTeamId(Long teamId);
    List<MatchResultDto> getMatchResultsByResult(String result);
    void deleteMatchResult(Long matchResultId);
}


