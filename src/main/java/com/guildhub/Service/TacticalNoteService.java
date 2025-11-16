package com.guildhub.Service;

import com.guildhub.Dto.TacticalNoteDto;

import java.util.List;

public interface TacticalNoteService {
    TacticalNoteDto createTacticalNote(TacticalNoteDto dto);
    TacticalNoteDto updateTacticalNote(Long noteId, TacticalNoteDto dto);
    TacticalNoteDto getTacticalNoteById(Long noteId);
    List<TacticalNoteDto> getAllTacticalNotes();
    List<TacticalNoteDto> getTacticalNotesByTeamId(Long teamId);
    List<TacticalNoteDto> getPublicTacticalNotesByTeamId(Long teamId);
    List<TacticalNoteDto> getTacticalNotesByMap(String map);
    void deleteTacticalNote(Long noteId);
}


