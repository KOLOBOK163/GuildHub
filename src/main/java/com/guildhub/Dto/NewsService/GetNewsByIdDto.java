package com.guildhub.Dto.NewsService;

import com.guildhub.newsservice.grpc.News;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetNewsByIdDto {
    private NewsDto news;

    public GetNewsByIdDto(NewsDto news) {
        this.news = news;
    }
}
