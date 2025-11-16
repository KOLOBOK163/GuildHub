package com.guildhub.Mapper;

import com.guildhub.Dto.TacticalNoteDto;
import com.guildhub.Entity.TacticalNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TacticalNoteMapper {
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TacticalNote tacticalNoteDtoToTacticalNote(TacticalNoteDto dto);
    
    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "author.id", target = "authorId")
    TacticalNoteDto tacticalNoteToTacticalNoteDto(TacticalNote note);
}


