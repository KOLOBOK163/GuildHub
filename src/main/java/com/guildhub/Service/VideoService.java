package com.guildhub.Service;

import com.guildhub.Dto.VideoDto;

import java.util.List;

public interface VideoService {
    VideoDto createVideo(VideoDto dto);
    VideoDto updateVideo(Long videoId, VideoDto dto);
    VideoDto getVideoById(Long videoId);
    List<VideoDto> getAllVideos();
    List<VideoDto> getVideosByTeamId(Long teamId);
    List<VideoDto> getVideosByType(String type);
    List<VideoDto> getVideosByMap(String map);
    VideoDto incrementViews(Long videoId);
    VideoDto incrementLikes(Long videoId);
    void deleteVideo(Long videoId);
}


