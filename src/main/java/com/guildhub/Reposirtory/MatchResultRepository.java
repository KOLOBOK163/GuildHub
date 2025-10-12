package com.guildhub.Reposirtory;

import com.guildhub.Entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
    List<MatchResult> findByTeamId(Long teamId);
    List<MatchResult> findByTeamIdOrderByMatchDateDesc(Long teamId);
    List<MatchResult> findByResult(String result);
}
