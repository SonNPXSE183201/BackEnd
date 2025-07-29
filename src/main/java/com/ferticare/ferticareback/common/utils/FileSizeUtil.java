package com.ferticare.ferticareback.common.utils;

import com.ferticare.ferticareback.common.constant.DataPatternConstant;

import java.io.File;
import java.text.DecimalFormat;

public final class FileSizeUtil {
    
    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB"};
    private static final long BYTE_SIZE = 1024;
    private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.#");
    
    /**
     * Format file size to human readable string
     */
    public static String formatSize(long bytes) {
        if (bytes == 0) {
            return "0 B";
        }
        
        int unitIndex = (int) (Math.log(bytes) / Math.log(BYTE_SIZE));
        double size = bytes / Math.pow(BYTE_SIZE, unitIndex);
        
        return FORMAT.format(size) + " " + SIZE_UNITS[unitIndex];
    }
    
    /**
     * Check if file size is within limit for images
     */
    public static boolean isValidImageSize(long fileSize) {
        return fileSize <= DataPatternConstant.MAX_IMAGE_SIZE;
    }
    
    /**
     * Check if file size is within limit for documents
     */
    public static boolean isValidDocumentSize(long fileSize) {
        return fileSize <= DataPatternConstant.MAX_DOCUMENT_SIZE;
    }
    
    /**
     * Check if file size is within limit for videos
     */
    public static boolean isValidVideoSize(long fileSize) {
        return fileSize <= DataPatternConstant.MAX_VIDEO_SIZE;
    }
    
    /**
     * Get file size from File object
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        return file.length();
    }
    
    /**
     * Check if file extension is valid for images
     */
    public static boolean isValidImageExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        
        String extension = getFileExtension(fileName).toLowerCase();
        for (String validExtension : DataPatternConstant.IMAGE_EXTENSIONS) {
            if (validExtension.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if file extension is valid for documents
     */
    public static boolean isValidDocumentExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        
        String extension = getFileExtension(fileName).toLowerCase();
        for (String validExtension : DataPatternConstant.DOCUMENT_EXTENSIONS) {
            if (validExtension.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get file extension from filename
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        
        return fileName.substring(lastDotIndex);
    }
    
    // Prevent instantiation
    private FileSizeUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }
} 