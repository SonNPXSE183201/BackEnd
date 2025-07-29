package com.ferticare.ferticareback.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DateFormatException extends RuntimeException {
    private final String messageCode;

    public DateFormatException(String messageCode, String message, Throwable casue) {
        super(message, casue);
        this.messageCode = messageCode;
    }
}