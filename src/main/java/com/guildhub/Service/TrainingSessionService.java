package com.guildhub.Service;

import com.guildhub.Dto.TrainingSessionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainingSessionService {
    TrainingSessionDto createTrainingSession(TrainingSessionDto dto);
    TrainingSessionDto updateTrainingSession(Long sessionId, TrainingSessionDto dto);
    TrainingSessionDto getTrainingSessionById(Long sessionId);
    List<TrainingSessionDto> getAllTrainingSessions();
    List<TrainingSessionDto> getTrainingSessionsByTeamId(Long teamId);
    List<TrainingSessionDto> getTrainingSessionsByDateRange(LocalDateTime start, LocalDateTime end);
    void deleteTrainingSession(Long sessionId);
}


