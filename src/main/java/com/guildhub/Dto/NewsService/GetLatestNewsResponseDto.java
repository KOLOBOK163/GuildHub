package com.guildhub.Dto.NewsService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetLatestNewsResponseDto {

    private List<NewsDto> news;

    public GetLatestNewsResponseDto(List<NewsDto> news) {
        this.news = news;
    }
}
