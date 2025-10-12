package com.guildhub.Reposirtory;

import com.guildhub.Entity.PlayerCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerCardRepository extends JpaRepository<PlayerCard, Long> {
    List<PlayerCard> findByTeamId(Long teamId);
    List<PlayerCard> findByNicknameContainingIgnoreCase(String nickname);
    List<PlayerCard> findByCountry(String country);
    List<PlayerCard> findByRatingGreaterThan(Double rating);
}
