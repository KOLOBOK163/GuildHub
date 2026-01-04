package com.guildhub.grpc.Controller;

import com.guildhub.Dto.NewsService.*;
import com.guildhub.grpc.NewsGrpcClient;
import com.guildhub.newsservice.grpc.CreateNewsRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NewsController {

    private final NewsGrpcClient newsGrpcClient;

    public NewsController(NewsGrpcClient newsGrpcClient) {
        this.newsGrpcClient = newsGrpcClient;
    }

    @PostMapping("/news")
    public CreateNewsResponseDto createNews(@Valid @RequestBody NewsDto newsDto) {
        return newsGrpcClient.createNewsAsDto(newsDto);
    }

    @PutMapping("/news/{id}")
    public UpdateNewsResponseDto updateNews(@PathVariable("id") Long id, @RequestBody NewsDto newsDto)
    {
        return newsGrpcClient.updateNewsAsDto(id, newsDto);
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

    @GetMapping("/news/{id}")
    public GetNewsByIdDto getNewsById(@PathVariable("id") Long id) {
        return newsGrpcClient.getNewsByIdAsDto(id);
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable("id") Long id) {
        newsGrpcClient.deleteNewsById(id);
        return ResponseEntity.noContent().build();
    }
}
