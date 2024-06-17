package com.cerc.tio.libcdpcommon.util;

import org.junit.jupiter.api.Test;

import static com.cerc.tio.libcdpcommon.util.JSONUtil.isValidJson;
import static org.junit.jupiter.api.Assertions.*;

class JSONUtilTest {

    @Test
    void isValidJson_shouldReturnFalse_whenStringIsNotAValidJson() {
        assertFalse(isValidJson("key: value"));
    }

    @Test
    void isValidJson_shouldReturnTrue_whenStringIsAValidJson() {
        assertTrue(isValidJson("{\"key\": \"value\"}"));
        assertTrue(isValidJson("[{\"key\": \"value\"}]"));
    }

}