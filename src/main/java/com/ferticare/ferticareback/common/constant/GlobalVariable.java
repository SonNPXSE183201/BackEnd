package com.ferticare.ferticareback.common.constant;

public final class GlobalVariable {

    public static final int PAGE_SIZE_INDEX = 1;

    // Application constants
    public static final String APP_NAME = "FertiCare";
    public static final String APP_VERSION = "1.0.0";
    public static final String API_VERSION = "v1";
    public static final String API_PREFIX = "/api/" + API_VERSION;
    
    // Security constants
    public static final String JWT_SECRET_KEY = "ferticare_secret_key_2024";
    public static final long JWT_EXPIRATION_TIME = 900000; // 15 phut in milliseconds
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    
    // CORS settings
    public static final String[] ALLOWED_ORIGINS = {"http://localhost:3000", "http://localhost:8080"};
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    public static final String[] ALLOWED_HEADERS = {"*"};
    
    // Pagination defaults
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    
    // System roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_DOCTOR = "DOCTOR";
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    
    // Status values
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_DELETED = "DELETED";
    
    // Prevent instantiation
    private GlobalVariable() {
        throw new AssertionError("Cannot instantiate constants class");
    }
} 