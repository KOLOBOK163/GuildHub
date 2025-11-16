package com.guildhub.Mapper;

import com.guildhub.Dto.TeamDto;
import com.guildhub.Entity.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team teamDtoToTeam(TeamDto teamDto);
    TeamDto teamToTeamDto(Team team);
}


