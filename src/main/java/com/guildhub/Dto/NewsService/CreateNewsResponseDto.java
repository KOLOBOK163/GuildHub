package com.guildhub.Dto.NewsService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CreateNewsResponseDto {

    private NewsDto news;

    public CreateNewsResponseDto(NewsDto news) {
        this.news = news;
    }
}
