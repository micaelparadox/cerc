package com.cerc.tio.financial.asset.tr.layout.middleware.controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.cerc.tio.financial.asset.tr.layout.middleware.exception.FailedBatchTransactionException;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class GlobalExceptionResponseHandlerTest {

    private GlobalExceptionResponseHandler handler;
    private FailedBatchTransactionException exception;
    private TagErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionResponseHandler();
        errorResponse = TagErrorResponse.builder().build();
        exception = new FailedBatchTransactionException(errorResponse);
    }

    @Test
    void testHandleFailedBatchTransactionException() {
        ResponseEntity<TagErrorResponse> response = handler.handleFailedBatchTransactionException(exception);

        assertEquals("Expected HTTP status 400 Bad Request", 400, response.getStatusCodeValue());
        assertEquals("Expected error message in body", errorResponse, response.getBody());
    }
}
