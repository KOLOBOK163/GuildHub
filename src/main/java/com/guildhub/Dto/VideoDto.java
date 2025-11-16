package com.guildhub.Dto;

import com.guildhub.Entity.VideoType.VideoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VideoDto {
    private Long id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    private LocalDate createdAt;
    
    @NotNull(message = "Video type is required")
    private VideoType type;
    
    private String youtubeUrl;
    private String map;
    private String position;
    private Long teamId;
    private Integer views = 0;
    private Integer likes = 0;
}


