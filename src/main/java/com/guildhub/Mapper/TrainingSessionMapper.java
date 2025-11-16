package com.guildhub.Mapper;

import com.guildhub.Dto.TrainingSessionDto;
import com.guildhub.Entity.TrainingSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingSessionMapper {
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "attendance", ignore = true)
    TrainingSession trainingSessionDtoToTrainingSession(TrainingSessionDto dto);
    
    @Mapping(source = "team.id", target = "teamId")
    TrainingSessionDto trainingSessionToTrainingSessionDto(TrainingSession session);
}


