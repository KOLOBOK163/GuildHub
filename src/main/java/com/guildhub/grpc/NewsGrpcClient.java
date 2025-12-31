package com.guildhub.grpc;

import com.guildhub.Dto.GetAllNewsResponseDto;
import com.guildhub.Dto.GetLatestNewsResponseDto;
import com.guildhub.Dto.NewsDto;
import com.guildhub.newsservice.grpc.*;
import org.springframework.stereotype.Component;
import com.guildhub.newsservice.grpc.NewsProto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class NewsGrpcClient {
    @GrpcClient("grpc-news-service")
    private NewsServiceGrpc.NewsServiceBlockingStub newsServiceStub;

    public GetLatestNewsResponse getLatestNews(int limit) {
        GetLatestNewsRequest request = GetLatestNewsRequest.newBuilder()
                .setLimit(limit)
                .build();
        return newsServiceStub.getLatestNews(request);
    }

    public GetAllNewsResponse getAllNews(int page, int pageSize) {
        GetAllNewsRequest request = GetAllNewsRequest.newBuilder()
                .setPage(page)
                .setPageSize(pageSize)
                .build();
        return newsServiceStub.getAllNews(request);
    }

    public GetLatestNewsResponseDto getLatestNewsAsDto(int limit) {
        GetLatestNewsResponse response = getLatestNews(limit);

        List<NewsDto> news = response.getNewsList().stream()
                .map(proto -> new NewsDto(
                        proto.getId(),
                        proto.getTitle(),
                        proto.getContent(),
                        proto.getAuthor(),
                        proto.getImageUrl(),
                        proto.getIsNew(),
                        Instant.ofEpochSecond(proto.getCreatedAt().getSeconds())
                ))
                .collect(Collectors.toList());

        return new GetLatestNewsResponseDto(news);
    }

    public GetAllNewsResponseDto getAllNewsAsDto(int page, int pageSize) {
        GetAllNewsResponse response = getAllNews(page, pageSize);

        List<NewsDto> news = response.getNewsList().stream()
                .map(proto -> new NewsDto(
                        proto.getId(),
                        proto.getTitle(),
                        proto.getContent(),
                        proto.getAuthor(),
                        proto.getImageUrl(),
                        proto.getIsNew(),
                        Instant.ofEpochSecond(proto.getCreatedAt().getSeconds())
                ))
                .collect(Collectors.toList());
        return new GetAllNewsResponseDto(news);
    }
}
