package com.ferticare.ferticareback.projectmanagementservice.blogmanagement.controller;

import com.ferticare.ferticareback.common.utils.FileStorageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/blog-images")
@RequiredArgsConstructor
public class BlogImageController {

    private final FileStorageUtil fileStorageUtil;

    @Operation(summary = "Upload ảnh blog", description = "Upload file ảnh blog dạng multipart/form-data")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "File ảnh", required = true)
            @RequestPart("file") MultipartFile file) {
        try {
            String fileName = fileStorageUtil.storeBlogImage(file);
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("fileUrl", "/api/blog-images/" + fileName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{imageFolder}/{imageName:.+}")
    public ResponseEntity<?> getImage(@PathVariable String imageFolder, @PathVariable String imageName) {
        try {
            Path imagePath = fileStorageUtil.getBlogImagePath(imageFolder + "/" + imageName);

            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);
            String contentType = determineContentType(imageName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String determineContentType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }
}