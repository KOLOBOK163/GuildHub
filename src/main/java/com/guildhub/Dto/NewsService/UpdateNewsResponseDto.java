package com.guildhub.Dto.NewsService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNewsResponseDto {

    private NewsDto news;

    public UpdateNewsResponseDto(NewsDto news) {
        this.news = news;
    }
}
