package com.guildhub.Mapper;

import com.guildhub.Dto.MatchResultDto;
import com.guildhub.Entity.MatchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchResultMapper {
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "opponentTeam", ignore = true)
    MatchResult matchResultDtoToMatchResult(MatchResultDto dto);
    
    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "opponentTeam.id", target = "opponentTeamId")
    MatchResultDto matchResultToMatchResultDto(MatchResult matchResult);
}


