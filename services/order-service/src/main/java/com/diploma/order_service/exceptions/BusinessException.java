package com.diploma.order_service.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class BusinessException extends RuntimeException {
    private final String errorMessage;

    public BusinessException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public BusinessException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
    }
}
