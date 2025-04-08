package com.diploma.order_service.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class BusinessException extends RuntimeException{
    private final String errorMessage;
}
