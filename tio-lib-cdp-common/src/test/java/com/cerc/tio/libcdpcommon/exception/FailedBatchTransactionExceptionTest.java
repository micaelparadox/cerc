package com.cerc.tio.libcdpcommon.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FailedBatchTransactionExceptionTest {

    @Test
    void FailedBatchTransactionException_shouldBuildATagErrorResponse() {
        FailedBatchTransactionException exception = FailedBatchTransactionException.of(null, null);

        assertNotNull(exception.getTagErrorResponse());
    }

}