package com.guildhub.Dto.NewsService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private long id;
    private String title;
    private String content;
    private String author;
    private String imageUrl;
    private Boolean isNew;
    private Instant createdAt;
    private Instant updatedAt;
}
