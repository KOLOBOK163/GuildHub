package com.guildhub.Service;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {
    String upload(String bucket, String objectName, MultipartFile file);
    void delete(String bucket, String objectName);
    String getPresignedUrl(String bucket, String objectName, int expirySeconds);
    void ensureBucket(String bucket);
}
