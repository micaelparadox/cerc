package com.cerc.tio.financial.asset.tr.layout.middleware.controller;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagErrorResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.exception.FailedBatchTransactionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class is a global exception handler for the application.
 * It catches exceptions that are not caught at the controller level.
 * The @ControllerAdvice annotation makes it applicable to all controllers in the application.
 */
@ControllerAdvice
public class GlobalExceptionResponseHandler {

    /**
     * This method handles {@link FailedBatchTransactionException}.
     * When a {@link FailedBatchTransactionException} is thrown anywhere in the application,
     * this method will be invoked to handle it.
     *
     * @param e The exception that is caught.
     * @return A {@link ResponseEntity} containing an error response body.
     */
    @ExceptionHandler(FailedBatchTransactionException.class)
    public ResponseEntity<TagErrorResponse> handleFailedBatchTransactionException(FailedBatchTransactionException e) {
        return ResponseEntity.badRequest().body(e.getTagErrorResponse());
    }
}
