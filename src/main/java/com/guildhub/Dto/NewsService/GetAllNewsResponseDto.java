package com.guildhub.Dto.NewsService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetAllNewsResponseDto {
    private List<NewsDto> news;
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private boolean last;

    public GetAllNewsResponseDto(List<NewsDto> news, int page, int size, long totalCount) {
        this.news = news;
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = (int) Math.ceil((double) totalCount / size);
        this.last = page >= this.totalPages - 1;
    }
}
