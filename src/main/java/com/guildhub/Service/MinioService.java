package com.guildhub.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class MinioService {

    private final ObjectStorageService storageService;

    @Value("${minio.buckets.avatars}")
    private String avatarsBucket;

    @Value("${minio.endpoint}")
    private String endpoint;

    public MinioService(ObjectStorageService storageService) {
        this.storageService = storageService;
    }

    public void deleteAvatar(String avatarUrl) {
        try {
            if (avatarUrl == null || avatarUrl.isBlank()) {
                return;
            }
            URI uri = URI.create(avatarUrl);
            String path = uri.getPath(); // /bucket/object/key
            if (path == null || path.isBlank()) {
                return;
            }
            String normalized = path.startsWith("/") ? path.substring(1) : path;
            int firstSlash = normalized.indexOf('/');
            if (firstSlash <= 0) {
                return;
            }
            String bucket = normalized.substring(0, firstSlash);
            String object = normalized.substring(firstSlash + 1);
            storageService.delete(bucket, object);
        } catch (Exception ignored) {
        }
    }
}
