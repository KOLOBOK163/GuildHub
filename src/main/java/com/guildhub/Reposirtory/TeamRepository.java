package com.guildhub.Reposirtory;

import com.guildhub.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);
}
