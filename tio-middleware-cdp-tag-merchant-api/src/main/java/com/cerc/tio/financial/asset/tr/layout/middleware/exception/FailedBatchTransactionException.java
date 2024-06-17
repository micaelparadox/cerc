package com.cerc.tio.financial.asset.tr.layout.middleware.exception;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class represents a custom exception that is thrown when a batch transaction fails.
 * It extends {@link RuntimeException}, meaning it is an unchecked exception.
 * This is meant to be used with {@link org.springframework.web.bind.annotation.ControllerAdvice} to handle the exception globally.
 */
@Getter
@RequiredArgsConstructor
public class FailedBatchTransactionException extends RuntimeException{
    private final transient TagErrorResponse tagErrorResponse;
}
