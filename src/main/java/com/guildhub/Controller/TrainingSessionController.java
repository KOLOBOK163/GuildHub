package com.guildhub.Controller;

import com.guildhub.Dto.TrainingSessionDto;
import com.guildhub.Service.TrainingSessionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/training-sessions")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    public TrainingSessionController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    @PostMapping
    public ResponseEntity<TrainingSessionDto> createTrainingSession(
            @Valid @RequestBody TrainingSessionDto dto) {
        return ResponseEntity.ok(trainingSessionService.createTrainingSession(dto));
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<TrainingSessionDto> updateTrainingSession(
            @PathVariable Long sessionId,
            @Valid @RequestBody TrainingSessionDto dto) {
        return ResponseEntity.ok(trainingSessionService.updateTrainingSession(sessionId, dto));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<TrainingSessionDto> getTrainingSessionById(@PathVariable Long sessionId) {
        return ResponseEntity.ok(trainingSessionService.getTrainingSessionById(sessionId));
    }

    @GetMapping
    public ResponseEntity<List<TrainingSessionDto>> getAllTrainingSessions() {
        return ResponseEntity.ok(trainingSessionService.getAllTrainingSessions());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TrainingSessionDto>> getTrainingSessionsByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(trainingSessionService.getTrainingSessionsByTeamId(teamId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TrainingSessionDto>> getTrainingSessionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(trainingSessionService.getTrainingSessionsByDateRange(start, end));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteTrainingSession(@PathVariable Long sessionId) {
        trainingSessionService.deleteTrainingSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}


