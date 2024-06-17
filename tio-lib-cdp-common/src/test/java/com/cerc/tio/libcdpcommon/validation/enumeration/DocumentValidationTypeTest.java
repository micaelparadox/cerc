package com.cerc.tio.libcdpcommon.validation.enumeration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentValidationTypeTest {

    @ParameterizedTest
    @EnumSource(DocumentValidationType.class)
    void testFromString(DocumentValidationType type) {
        DocumentValidationType result = DocumentValidationType.fromString(type.name());
        assertEquals(type, result);
    }

}