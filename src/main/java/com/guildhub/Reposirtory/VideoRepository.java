package com.guildhub.Reposirtory;

import com.guildhub.Entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByTeamId(Long teamId);
    List<Video> findByType(String type);
    List<Video> findByMap(String map);
    List<Video> findByViewsGreaterThan(Integer views);
    List<Video> findByLikesGreaterThan(Integer likes);
}
