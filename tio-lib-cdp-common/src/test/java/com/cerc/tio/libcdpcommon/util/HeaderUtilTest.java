package com.cerc.tio.libcdpcommon.util;

import com.cerc.tio.libcdpcommon.util.domain.ProcessingIds;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeaderUtilTest {

    @Test
    void serializeApigeeHeaders_Should_return_ProcessingIds_When_given_parameters() {
        // Given
        Map<String, String> headers = Map.of(
                "company-document", "companyDocument",
                "company-id", "companyId",
                "correlation-id", "correlationId"
        );

        // When
        ProcessingIds processingIds = HeaderUtil.serializeApigeeHeaders(headers);

        // Then
        assertEquals("companyDocument", processingIds.getCompanyDocument());
        assertEquals("companyId", processingIds.getCompanyId());
        assertEquals("correlationId", processingIds.getCorrelationId());
    }
}