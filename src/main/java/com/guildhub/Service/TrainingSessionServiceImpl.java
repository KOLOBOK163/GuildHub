package com.guildhub.Service;

import com.guildhub.Dto.TrainingSessionDto;
import com.guildhub.Entity.Team;
import com.guildhub.Entity.TrainingSession;
import com.guildhub.Mapper.TrainingSessionMapper;
import com.guildhub.Reposirtory.TeamRepository;
import com.guildhub.Reposirtory.TrainingSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingSessionServiceImpl implements TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TeamRepository teamRepository;
    private final TrainingSessionMapper trainingSessionMapper;

    public TrainingSessionServiceImpl(
            TrainingSessionRepository trainingSessionRepository,
            TeamRepository teamRepository,
            TrainingSessionMapper trainingSessionMapper) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.teamRepository = teamRepository;
        this.trainingSessionMapper = trainingSessionMapper;
    }

    @Override
    @Transactional
    public TrainingSessionDto createTrainingSession(TrainingSessionDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));

        TrainingSession session = trainingSessionMapper.trainingSessionDtoToTrainingSession(dto);
        session.setTeam(team);

        return trainingSessionMapper.trainingSessionToTrainingSessionDto(
                trainingSessionRepository.save(session));
    }

    @Override
    @Transactional
    public TrainingSessionDto updateTrainingSession(Long sessionId, TrainingSessionDto dto) {
        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Training session not found with id: " + sessionId));

        if (dto.getTeamId() != null && !session.getTeam().getId().equals(dto.getTeamId())) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));
            session.setTeam(team);
        }

        session.setDateTime(dto.getDateTime());
        session.setDescription(dto.getDescription());
        session.setType(dto.getType());
        session.setOpponent(dto.getOpponent());
        session.setServerInfo(dto.getServerInfo());

        return trainingSessionMapper.trainingSessionToTrainingSessionDto(
                trainingSessionRepository.save(session));
    }

    @Override
    public TrainingSessionDto getTrainingSessionById(Long sessionId) {
        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Training session not found with id: " + sessionId));
        return trainingSessionMapper.trainingSessionToTrainingSessionDto(session);
    }

    @Override
    public List<TrainingSessionDto> getAllTrainingSessions() {
        return trainingSessionRepository.findAll().stream()
                .map(trainingSessionMapper::trainingSessionToTrainingSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingSessionDto> getTrainingSessionsByTeamId(Long teamId) {
        return trainingSessionRepository.findByTeamIdOrderByDateTimeDesc(teamId).stream()
                .map(trainingSessionMapper::trainingSessionToTrainingSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingSessionDto> getTrainingSessionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return trainingSessionRepository.findByDateTimeBetween(start, end).stream()
                .map(trainingSessionMapper::trainingSessionToTrainingSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTrainingSession(Long sessionId) {
        if (!trainingSessionRepository.existsById(sessionId)) {
            throw new EntityNotFoundException("Training session not found with id: " + sessionId);
        }
        trainingSessionRepository.deleteById(sessionId);
    }
}


