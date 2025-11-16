package com.guildhub.Controller;

import com.guildhub.Dto.VideoDto;
import com.guildhub.Service.VideoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<VideoDto> createVideo(@Valid @RequestBody VideoDto dto) {
        return ResponseEntity.ok(videoService.createVideo(dto));
    }

    @PutMapping("/{videoId}")
    public ResponseEntity<VideoDto> updateVideo(
            @PathVariable Long videoId,
            @Valid @RequestBody VideoDto dto) {
        return ResponseEntity.ok(videoService.updateVideo(videoId, dto));
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDto> getVideoById(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.getVideoById(videoId));
    }

    @GetMapping
    public ResponseEntity<List<VideoDto>> getAllVideos() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<VideoDto>> getVideosByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(videoService.getVideosByTeamId(teamId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<VideoDto>> getVideosByType(@PathVariable String type) {
        return ResponseEntity.ok(videoService.getVideosByType(type));
    }

    @GetMapping("/map/{map}")
    public ResponseEntity<List<VideoDto>> getVideosByMap(@PathVariable String map) {
        return ResponseEntity.ok(videoService.getVideosByMap(map));
    }

    @PostMapping("/{videoId}/views")
    public ResponseEntity<VideoDto> incrementViews(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.incrementViews(videoId));
    }

    @PostMapping("/{videoId}/likes")
    public ResponseEntity<VideoDto> incrementLikes(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.incrementLikes(videoId));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return ResponseEntity.noContent().build();
    }
}


