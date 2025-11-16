package com.guildhub.Mapper;

import com.guildhub.Dto.TeamStatisticDto;
import com.guildhub.Entity.TeamStatistic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamStatisticMapper {
    @Mapping(target = "team", ignore = true)
    TeamStatistic teamStatisticDtoToTeamStatistic(TeamStatisticDto dto);
    
    @Mapping(source = "team.id", target = "teamId")
    TeamStatisticDto teamStatisticToTeamStatisticDto(TeamStatistic statistic);
}


