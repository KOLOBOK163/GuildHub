package com.guildhub.grpc.Controller;

import com.guildhub.Dto.GetAllNewsResponseDto;
import com.guildhub.Dto.GetLatestNewsResponseDto;
import com.guildhub.grpc.NewsGrpcClient;
import com.guildhub.newsservice.grpc.GetAllNewsResponse;
import com.guildhub.newsservice.grpc.GetLatestNewsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NewsController {

    private final NewsGrpcClient newsGrpcClient;

    public NewsController(NewsGrpcClient newsGrpcClient) {
        this.newsGrpcClient = newsGrpcClient;
    }

    @GetMapping("/home/news")
    public GetLatestNewsResponseDto getLatestNewsForHome(@RequestParam(defaultValue = "5") int limit) {
        return newsGrpcClient.getLatestNewsAsDto(limit);
    }

    @GetMapping("/news")
    public GetAllNewsResponseDto getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return newsGrpcClient.getAllNewsAsDto(page, size);
    }
}
