package com.ferticare.ferticareback.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AppException
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {

    private final String messageCode;

    public AppException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }

    public AppException(String messageCode, String message, Throwable cause){
        super(message,cause);
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

}