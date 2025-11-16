package com.guildhub.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TacticalNoteDto {
    private Long id;
    
    @NotNull(message = "Team ID is required")
    private Long teamId;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String content;
    private String map;
    private String position;
    private Long authorId;
    private boolean isPrivate = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


