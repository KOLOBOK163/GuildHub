package com.guildhub.Service;

import com.guildhub.Dto.AttendanceDto;
import com.guildhub.Entity.Attendance;
import com.guildhub.Entity.PlayerCard;
import com.guildhub.Entity.TrainingSession;
import com.guildhub.Mapper.AttendanceMapper;
import com.guildhub.Reposirtory.AttendanceRepository;
import com.guildhub.Reposirtory.PlayerCardRepository;
import com.guildhub.Reposirtory.TrainingSessionRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final PlayerCardRepository playerCardRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            TrainingSessionRepository trainingSessionRepository,
            PlayerCardRepository playerCardRepository,
            AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.trainingSessionRepository = trainingSessionRepository;
        this.playerCardRepository = playerCardRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    @Transactional
    public AttendanceDto createAttendance(AttendanceDto dto) {
        // Check if attendance already exists for this session and player
        boolean exists = attendanceRepository.findByTrainingSessionId(dto.getTrainingSessionId())
                .stream()
                .anyMatch(a -> a.getPlayerCard().getId().equals(dto.getPlayerCardId()));
        
        if (exists) {
            throw new EntityExistsException("Attendance already exists for this training session and player");
        }

        TrainingSession trainingSession = trainingSessionRepository.findById(dto.getTrainingSessionId())
                .orElseThrow(() -> new EntityNotFoundException("Training session not found with id: " + dto.getTrainingSessionId()));

        PlayerCard playerCard = playerCardRepository.findById(dto.getPlayerCardId())
                .orElseThrow(() -> new EntityNotFoundException("Player card not found with id: " + dto.getPlayerCardId()));

        Attendance attendance = attendanceMapper.attendanceDtoToAttendance(dto);
        attendance.setTrainingSession(trainingSession);
        attendance.setPlayerCard(playerCard);

        return attendanceMapper.attendanceToAttendanceDto(attendanceRepository.save(attendance));
    }

    @Override
    @Transactional
    public AttendanceDto updateAttendance(Long attendanceId, AttendanceDto dto) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException("Attendance not found with id: " + attendanceId));

        if (dto.getTrainingSessionId() != null && 
            !attendance.getTrainingSession().getId().equals(dto.getTrainingSessionId())) {
            TrainingSession trainingSession = trainingSessionRepository.findById(dto.getTrainingSessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Training session not found with id: " + dto.getTrainingSessionId()));
            attendance.setTrainingSession(trainingSession);
        }

        if (dto.getPlayerCardId() != null && 
            !attendance.getPlayerCard().getId().equals(dto.getPlayerCardId())) {
            PlayerCard playerCard = playerCardRepository.findById(dto.getPlayerCardId())
                    .orElseThrow(() -> new EntityNotFoundException("Player card not found with id: " + dto.getPlayerCardId()));
            attendance.setPlayerCard(playerCard);
        }

        attendance.setAttended(dto.isAttended());
        attendance.setReason(dto.getReason());
        attendance.setPerformance(dto.getPerformance());
        attendance.setNotes(dto.getNotes());

        return attendanceMapper.attendanceToAttendanceDto(attendanceRepository.save(attendance));
    }

    @Override
    public AttendanceDto getAttendanceById(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException("Attendance not found with id: " + attendanceId));
        return attendanceMapper.attendanceToAttendanceDto(attendance);
    }

    @Override
    public List<AttendanceDto> getAllAttendances() {
        return attendanceRepository.findAll().stream()
                .map(attendanceMapper::attendanceToAttendanceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> getAttendancesByTrainingSessionId(Long trainingSessionId) {
        return attendanceRepository.findByTrainingSessionId(trainingSessionId).stream()
                .map(attendanceMapper::attendanceToAttendanceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> getAttendancesByPlayerCardId(Long playerCardId) {
        return attendanceRepository.findByPlayerCardId(playerCardId).stream()
                .map(attendanceMapper::attendanceToAttendanceDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAttendance(Long attendanceId) {
        if (!attendanceRepository.existsById(attendanceId)) {
            throw new EntityNotFoundException("Attendance not found with id: " + attendanceId);
        }
        attendanceRepository.deleteById(attendanceId);
    }
}


