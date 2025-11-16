package com.guildhub.Controller;

import com.guildhub.Dto.TacticalNoteDto;
import com.guildhub.Service.TacticalNoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tactical-notes")
public class TacticalNoteController {

    private final TacticalNoteService tacticalNoteService;

    public TacticalNoteController(TacticalNoteService tacticalNoteService) {
        this.tacticalNoteService = tacticalNoteService;
    }

    @PostMapping
    public ResponseEntity<TacticalNoteDto> createTacticalNote(@Valid @RequestBody TacticalNoteDto dto) {
        return ResponseEntity.ok(tacticalNoteService.createTacticalNote(dto));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<TacticalNoteDto> updateTacticalNote(
            @PathVariable Long noteId,
            @Valid @RequestBody TacticalNoteDto dto) {
        return ResponseEntity.ok(tacticalNoteService.updateTacticalNote(noteId, dto));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<TacticalNoteDto> getTacticalNoteById(@PathVariable Long noteId) {
        return ResponseEntity.ok(tacticalNoteService.getTacticalNoteById(noteId));
    }

    @GetMapping
    public ResponseEntity<List<TacticalNoteDto>> getAllTacticalNotes() {
        return ResponseEntity.ok(tacticalNoteService.getAllTacticalNotes());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TacticalNoteDto>> getTacticalNotesByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(tacticalNoteService.getTacticalNotesByTeamId(teamId));
    }

    @GetMapping("/team/{teamId}/public")
    public ResponseEntity<List<TacticalNoteDto>> getPublicTacticalNotesByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(tacticalNoteService.getPublicTacticalNotesByTeamId(teamId));
    }

    @GetMapping("/map/{map}")
    public ResponseEntity<List<TacticalNoteDto>> getTacticalNotesByMap(@PathVariable String map) {
        return ResponseEntity.ok(tacticalNoteService.getTacticalNotesByMap(map));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteTacticalNote(@PathVariable Long noteId) {
        tacticalNoteService.deleteTacticalNote(noteId);
        return ResponseEntity.noContent().build();
    }
}


