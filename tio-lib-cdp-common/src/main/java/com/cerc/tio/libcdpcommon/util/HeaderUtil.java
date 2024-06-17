package com.cerc.tio.libcdpcommon.util;

import com.cerc.tio.libcdpcommon.util.domain.ProcessingIds;

import java.util.Map;
import java.util.UUID;

import static java.util.Optional.ofNullable;

public class HeaderUtil {
    private static final String COMPANY_DOCUMENT = "company-document";
    private static final String COMPANY_ID = "company-id";
    private static final String CORRELATION_ID = "correlation-id";

    private HeaderUtil() {}

    /**
     * Serializes headers from Apigee into a {@link ProcessingIds} object.
     * The headers map should contain keys for company document, company id, and correlation id.
     * If the correlation id is not present in the headers, a random UUID is generated and used.
     *
     * @param headers The map of headers.
     * @return A {@link ProcessingIds} object containing the values from the headers.
     */
    public static ProcessingIds serializeApigeeHeaders(Map<String, String> headers) {
        return new ProcessingIds(
            headers.get(COMPANY_DOCUMENT),
            headers.get(COMPANY_ID),
            ofNullable(headers.get(CORRELATION_ID)).orElse(UUID.randomUUID().toString())
        );
    }
}
