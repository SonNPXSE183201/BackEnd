package com.ferticare.ferticareback.common.constant;

public final class MessageConstant {
    
    // Success messages
    public static final String SUCCESS = "Operation completed successfully";
    public static final String CREATED_SUCCESS = "Created successfully";
    public static final String UPDATED_SUCCESS = "Updated successfully";
    public static final String DELETED_SUCCESS = "Deleted successfully";
    
    // Error messages
    public static final String NOT_FOUND = "Resource not found";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Access denied";
    public static final String BAD_REQUEST = "Invalid request";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    
    // User related messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String ACCOUNT_LOCKED = "Account is locked";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    
    // Authentication messages
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String INVALID_TOKEN = "Invalid token";
    
    // Validation messages
    public static final String REQUIRED_FIELD = "This field is required";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String INVALID_PHONE = "Invalid phone number format";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters long";
    public static final String PASSWORD_MISMATCH = "Passwords do not match";
    
    // Prevent instantiation
    private MessageConstant() {
        throw new AssertionError("Cannot instantiate constants class");
    }
} 