package com.guildhub.Reposirtory;

import com.guildhub.Entity.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    List<TrainingSession> findByTeamId(Long teamId);
    List<TrainingSession> findByTeamIdOrderByDateTimeDesc(Long teamId);
    List<TrainingSession> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<TrainingSession> findByType(String type);
}
