package com.guildhub.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetAllNewsResponseDto {
    private List<NewsDto> news;

    public GetAllNewsResponseDto(List<NewsDto> news) {
        this.news = news;
    }
}
