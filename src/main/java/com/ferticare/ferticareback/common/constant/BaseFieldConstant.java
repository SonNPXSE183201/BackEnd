package com.ferticare.ferticareback.common.constant;

public final class BaseFieldConstant {
    
    // Database field names
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String DELETED_AT = "deleted_at";
    public static final String IS_ACTIVE = "is_active";
    public static final String STATUS = "status";
    
    // Common field lengths
    public static final int SMALL_STRING_LENGTH = 50;
    public static final int MEDIUM_STRING_LENGTH = 100;
    public static final int LARGE_STRING_LENGTH = 255;
    public static final int EXTRA_LARGE_STRING_LENGTH = 500;
    public static final int TEXT_LENGTH = 1000;
    public static final int LONG_TEXT_LENGTH = 5000;
    
    // Prevent instantiation
    private BaseFieldConstant() {
        throw new AssertionError("Cannot instantiate constants class");
    }
} 