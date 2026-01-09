package com.guildhub.grpc.Controller;

import com.guildhub.Dto.NewsService.*;
import com.guildhub.grpc.NewsGrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class NewsController {

    private final NewsGrpcClient newsGrpcClient;

    public NewsController(NewsGrpcClient newsGrpcClient) {
        this.newsGrpcClient = newsGrpcClient;
    }

    @PostMapping(value = "/news", consumes = "multipart/form-data")
    public CreateNewsResponseDto createNews(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("author") String author,
            @RequestParam(value = "isNew", defaultValue = "false") Boolean isNew,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        
        NewsDto newsDto = new NewsDto();
        newsDto.setTitle(title);
        newsDto.setContent(content);
        newsDto.setAuthor(author);
        newsDto.setIsNew(isNew);
        
        byte[] imageData = null;
        String imageFileName = null;
        String imageContentType = null;
        
        if (image != null && !image.isEmpty()) {
            imageData = image.getBytes();
            imageFileName = image.getOriginalFilename();
            imageContentType = image.getContentType();
        }
        
        return newsGrpcClient.createNewsAsDto(newsDto, imageData, imageFileName, imageContentType);
    }

    @PutMapping(value = "/news/{id}", consumes = "multipart/form-data")
    public UpdateNewsResponseDto updateNews(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("author") String author,
            @RequestParam(value = "isNew", defaultValue = "false") Boolean isNew,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        
        NewsDto newsDto = new NewsDto();
        newsDto.setTitle(title);
        newsDto.setContent(content);
        newsDto.setAuthor(author);
        newsDto.setIsNew(isNew);
        
        byte[] imageData = null;
        String imageFileName = null;
        String imageContentType = null;
        
        if (image != null && !image.isEmpty()) {
            imageData = image.getBytes();
            imageFileName = image.getOriginalFilename();
            imageContentType = image.getContentType();
        }
        
        return newsGrpcClient.updateNewsAsDto(id, newsDto, imageData, imageFileName, imageContentType);
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
