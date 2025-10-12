package com.guildhub.Reposirtory;

import com.guildhub.Entity.TeamStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TeamStatisticRepository extends JpaRepository<TeamStatistic, Long> {
    List<TeamStatistic> findByTeamId(Long teamId);
    List<TeamStatistic> findByTeamIdAndPeriodStartBetween(Long teamId, LocalDate start, LocalDate end);
    List<TeamStatistic> findByTeamIdOrderByPeriodEndDesc(Long teamId);
}
