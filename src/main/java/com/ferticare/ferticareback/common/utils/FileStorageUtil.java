package com.ferticare.ferticareback.common.utils;

import com.ferticare.ferticareback.common.constant.DataPatternConstant;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileStorageUtil {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.blog-images-dir:blog-images}")
    private String blogImagesDir;

    private Path fileStorageLocation;
    private Path blogImagesLocation;

    @PostConstruct
    public void init() {
        try {
            this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(fileStorageLocation);

            this.blogImagesLocation = Paths.get(uploadDir, blogImagesDir).toAbsolutePath().normalize();
            Files.createDirectories(blogImagesLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Không thể tạo thư mục để lưu trữ file.", ex);
        }
    }

    public String storeBlogImage(MultipartFile file) {
        return storeFile(file, blogImagesLocation);
    }

    private String storeFile(MultipartFile file, Path location) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File không hợp lệ");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new RuntimeException("Tên file không hợp lệ");
        }

        // Kiểm tra kích thước file
        if (!FileSizeUtil.isValidImageSize(file.getSize())) {
            throw new RuntimeException("Kích thước file vượt quá giới hạn cho phép");
        }

        // Kiểm tra định dạng file
        if (!FileSizeUtil.isValidImageExtension(originalFileName)) {
            throw new RuntimeException("Chỉ cho phép tải lên file hình ảnh");
        }

        try {
            // Tạo tên file unique
            String extension = FileSizeUtil.getFileExtension(originalFileName);
            String fileName = UUID.randomUUID() + extension;

            Path targetLocation = location.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);

            return location.getFileName().toString() + "/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu file " + originalFileName, ex);
        }
    }

    public Path getBlogImagePath(String fileName) {
        return blogImagesLocation.resolve(fileName.replace(blogImagesDir + "/", "")).normalize();
    }

    public boolean deleteFile(Path filePath) {
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            return false;
        }
    }
}