package com.ferticare.ferticareback.common.constant;

public final class DataPatternConstant {
    
    // Regular expressions
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PHONE_PATTERN = "^(\\+84|0)[3-9]\\d{8}$"; // Vietnamese phone number
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,20}$";
    public static final String FULL_NAME_PATTERN = "^[a-zA-ZÀ-ỹĐđ\\s]{2,50}$"; // Vietnamese characters
    
    // Date formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    // Number formats
    public static final String CURRENCY_FORMAT = "#,##0.00";
    public static final String PERCENTAGE_FORMAT = "#,##0.00%";
    public static final String INTEGER_FORMAT = "#,##0";
    
    // File extensions
    public static final String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
    public static final String[] DOCUMENT_EXTENSIONS = {".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx"};
    public static final String[] VIDEO_EXTENSIONS = {".mp4", ".avi", ".mov", ".wmv", ".flv", ".webm"};
    
    // File size limits (in bytes)
    public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final long MAX_DOCUMENT_SIZE = 10 * 1024 * 1024; // 10MB
    public static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB
    
    // Prevent instantiation
    private DataPatternConstant() {
        throw new AssertionError("Cannot instantiate constants class");
    }
} 