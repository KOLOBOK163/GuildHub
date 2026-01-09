package com.guildhub.grpc;

import com.google.protobuf.ByteString;
import com.guildhub.Dto.NewsService.*;
import com.guildhub.newsservice.grpc.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
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

    public GetNewsByIdResponse getNewsById(Long id) {
        GetNewsByIdRequest request = GetNewsByIdRequest.newBuilder()
                .setId(id)
                .build();
        return newsServiceStub.getNewsById(request);
    }

    public CreateNewsResponseDto createNewsAsDto(NewsDto news, byte[] imageData, String imageFileName, String imageContentType) {
        CreateNewsRequest.Builder requestBuilder = CreateNewsRequest.newBuilder()
                .setTitle(news.getTitle())
                .setContent(news.getContent())
                .setAuthor(news.getAuthor())
                .setIsNew(news.getIsNew());
        
        if (imageData != null && imageData.length > 0) {
            requestBuilder.setImageData(ByteString.copyFrom(imageData));
        }
        if (imageFileName != null && !imageFileName.isEmpty()) {
            requestBuilder.setImageFilename(imageFileName);
        }
        if (imageContentType != null && !imageContentType.isEmpty()) {
            requestBuilder.setImageContentType(imageContentType);
        }
        
        CreateNewsResponse response = newsServiceStub.createNews(requestBuilder.build());


        News proto = response.getNews();

        NewsDto newsDto = new NewsDto(
                proto.getId(),
                proto.getTitle(),
                proto.getContent(),
                proto.getAuthor(),
                proto.getImageUrl(),
                proto.getIsNew(),
                Instant.ofEpochSecond(proto.getCreatedAt().getSeconds(), proto.getCreatedAt().getNanos()),
                null
        );
        return new CreateNewsResponseDto(newsDto);
    }

    public UpdateNewsResponseDto updateNewsAsDto(Long id, NewsDto news, byte[] imageData, String imageFilename, String imageContentType) {
        UpdateNewsByIdRequest.Builder requestBuilder = UpdateNewsByIdRequest.newBuilder()
                .setId(id)
                .setTitle(news.getTitle())
                .setContent(news.getContent())
                .setAuthor(news.getAuthor())
                .setIsNew(news.getIsNew());
        
        if (imageData != null && imageData.length > 0) {
            requestBuilder.setImageData(ByteString.copyFrom(imageData));
        }
        if (imageFilename != null && !imageFilename.isEmpty()) {
            requestBuilder.setImageFilename(imageFilename);
        }
        if (imageContentType != null && !imageContentType.isEmpty()) {
            requestBuilder.setImageContentType(imageContentType);
        }
        
        UpdateNewsByIdResponse response = newsServiceStub.updateNewsById(requestBuilder.build());

        News proto = response.getNews();
        NewsDto newsDto = new NewsDto(
                proto.getId(),
                proto.getTitle(),
                proto.getContent(),
                proto.getAuthor(),
                proto.getImageUrl(),
                proto.getIsNew(),
                Instant.ofEpochSecond(proto.getCreatedAt().getSeconds(), proto.getCreatedAt().getNanos()),
                proto.hasUpdatedAt()
                        ? Instant.ofEpochSecond(proto.getUpdatedAt().getSeconds(), proto.getUpdatedAt().getNanos())
                        : null
        );

        return new UpdateNewsResponseDto(newsDto);
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
                        Instant.ofEpochSecond(proto.getCreatedAt().getSeconds(), proto.getCreatedAt().getNanos()),
                        proto.hasUpdatedAt()
                                ? Instant.ofEpochSecond(proto.getUpdatedAt().getSeconds(), proto.getUpdatedAt().getNanos())
                                : null
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
                        Instant.ofEpochSecond(proto.getCreatedAt().getSeconds(), proto.getCreatedAt().getNanos()),
                        proto.hasUpdatedAt()
                                ? Instant.ofEpochSecond(proto.getUpdatedAt().getSeconds(), proto.getUpdatedAt().getNanos())
                                : null
                ))
                .collect(Collectors.toList());

        return new GetAllNewsResponseDto(news, page, pageSize, response.getTotalCount());
    }

    public GetNewsByIdDto getNewsByIdAsDto(Long id) {
        GetNewsByIdResponse response = getNewsById(id);
        News proto = response.getNews();

        NewsDto news = new NewsDto(
                proto.getId(),
                proto.getTitle(),
                proto.getContent(),
                proto.getAuthor(),
                proto.getImageUrl(),
                proto.getIsNew(),
                Instant.ofEpochSecond(
                        proto.getCreatedAt().getSeconds(),
                        proto.getCreatedAt().getNanos()
                ),
                proto.hasUpdatedAt()
                        ? Instant.ofEpochSecond(proto.getUpdatedAt().getSeconds(), proto.getUpdatedAt().getNanos())
                        : null
        );

        return new GetNewsByIdDto(news);
    }

    public boolean deleteNewsById(Long id) {
        try {
            DeleteNewsByIdRequest request = DeleteNewsByIdRequest.newBuilder()
                    .setId(id)
                    .build();
            DeleteNewsByIdResponse response = newsServiceStub.deleteNewsById(request);
            return response.getSuccess();
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new EntityNotFoundException("News not found: " + id);
            }
            throw e;
        }
    }
}
