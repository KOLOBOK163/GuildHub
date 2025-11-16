package com.guildhub.Service;

import com.guildhub.Dto.VideoDto;
import com.guildhub.Entity.Team;
import com.guildhub.Entity.Video;
import com.guildhub.Mapper.VideoMapper;
import com.guildhub.Reposirtory.TeamRepository;
import com.guildhub.Reposirtory.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final TeamRepository teamRepository;
    private final VideoMapper videoMapper;

    public VideoServiceImpl(
            VideoRepository videoRepository,
            TeamRepository teamRepository,
            VideoMapper videoMapper) {
        this.videoRepository = videoRepository;
        this.teamRepository = teamRepository;
        this.videoMapper = videoMapper;
    }

    @Override
    @Transactional
    public VideoDto createVideo(VideoDto dto) {
        Video video = videoMapper.videoDtoToVideo(dto);
        video.setCreatedAt(LocalDate.now());
        
        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));
            video.setTeam(team);
        }

        if (video.getViews() == null) {
            video.setViews(0);
        }
        if (video.getLikes() == null) {
            video.setLikes(0);
        }

        return videoMapper.videoToVideoDto(videoRepository.save(video));
    }

    @Override
    @Transactional
    public VideoDto updateVideo(Long videoId, VideoDto dto) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video not found with id: " + videoId));

        if (dto.getTeamId() != null && (video.getTeam() == null || !video.getTeam().getId().equals(dto.getTeamId()))) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));
            video.setTeam(team);
        }

        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setType(dto.getType());
        video.setYoutubeUrl(dto.getYoutubeUrl());
        video.setMap(dto.getMap());
        video.setPosition(dto.getPosition());

        return videoMapper.videoToVideoDto(videoRepository.save(video));
    }

    @Override
    public VideoDto getVideoById(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video not found with id: " + videoId));
        return videoMapper.videoToVideoDto(video);
    }

    @Override
    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(videoMapper::videoToVideoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VideoDto> getVideosByTeamId(Long teamId) {
        return videoRepository.findByTeamId(teamId).stream()
                .map(videoMapper::videoToVideoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VideoDto> getVideosByType(String type) {
        return videoRepository.findByType(type).stream()
                .map(videoMapper::videoToVideoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VideoDto> getVideosByMap(String map) {
        return videoRepository.findByMap(map).stream()
                .map(videoMapper::videoToVideoDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VideoDto incrementViews(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video not found with id: " + videoId));
        video.setViews(video.getViews() + 1);
        return videoMapper.videoToVideoDto(videoRepository.save(video));
    }

    @Override
    @Transactional
    public VideoDto incrementLikes(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video not found with id: " + videoId));
        video.setLikes(video.getLikes() + 1);
        return videoMapper.videoToVideoDto(videoRepository.save(video));
    }

    @Override
    @Transactional
    public void deleteVideo(Long videoId) {
        if (!videoRepository.existsById(videoId)) {
            throw new EntityNotFoundException("Video not found with id: " + videoId);
        }
        videoRepository.deleteById(videoId);
    }
}


