package com.guildhub.Reposirtory;

import com.guildhub.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByTrainingSessionId(Long trainingSessionId);
    List<Attendance> findByPlayerCardId(Long playerCardId);
    List<Attendance> findByAttended(boolean attended);
    List<Attendance> findByTrainingSessionIdAndAttended(Long trainingSessionId, boolean attended);
}
