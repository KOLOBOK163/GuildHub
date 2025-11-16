package com.guildhub.Controller;

import com.guildhub.Service.ObjectStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final ObjectStorageService storageService;

    public FileController(ObjectStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload/{bucket}")
    public ResponseEntity<Map<String, String>> upload(
            @PathVariable String bucket,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "prefix", required = false) String prefix
    ) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HHmmssSSS"));
        String safeFilename = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
        String objectName = (prefix == null || prefix.isBlank() ? "" : prefix.trim().replaceAll("^/+|/+$", "") + "/") + timestamp + "-" + safeFilename;
        String url = storageService.upload(bucket, objectName, file);
        return ResponseEntity.ok(Map.of("url", url, "object", objectName, "bucket", bucket));
    }
}
