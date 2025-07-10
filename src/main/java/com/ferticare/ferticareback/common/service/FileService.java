package com.ferticare.ferticareback.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file, String directory);
    void deleteFile(String filePath);
    boolean isValidImageFile(MultipartFile file);
} 