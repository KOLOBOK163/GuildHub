package com.guildhub.Service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class MinioStorageService implements ObjectStorageService {

    private final MinioClient minioClient;

    @Value("${minio.buckets.avatars}")
    private String avatarsBucket;

    @Value("${minio.buckets.videos}")
    private String videosBucket;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void ensureBucket(String bucket) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to ensure bucket: " + bucket, e);
        }
    }

    @Override
    public String upload(String bucket, String objectName, MultipartFile file) {
        ensureBucket(bucket);
        try (InputStream is = file.getInputStream()) {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(is, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(args);
            return getPresignedUrl(bucket, objectName, (int) TimeUnit.DAYS.toSeconds(7));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload object to MinIO", e);
        }
    }

    @Override
    public void delete(String bucket, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete object from MinIO", e);
        }
    }

    @Override
    public String getPresignedUrl(String bucket, String objectName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(objectName)
                    .expiry(expirySeconds)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create presigned URL", e);
        }
    }
}
