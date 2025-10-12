package com.guildhub.Reposirtory;

import com.guildhub.Entity.TacticalNote;
import com.guildhub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacticalNoteRepository extends JpaRepository<TacticalNote, Long> {
    List<TacticalNote> findByTeamId(Long teamId);
    List<TacticalNote> findByTeamIdAndIsPrivateFalse(Long teamId);
    List<TacticalNote> findByMap(String map);
    List<TacticalNote> findByAuthor(User author);
}
