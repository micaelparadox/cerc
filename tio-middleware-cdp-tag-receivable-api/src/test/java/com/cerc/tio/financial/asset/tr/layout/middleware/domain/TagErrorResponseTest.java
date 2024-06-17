package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagErrorResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serializeToJson_shouldSerializeCorrectly() throws Exception {
        // given
        TagErrorResponse errorResponse = TagErrorResponse.builder()
                .processKey("ErrorProcess123")
                .createdAt("2024-06-12")
                .errors(List.of("Error1", "Error2"))
                .build();

        // when
        String json = mapper.writeValueAsString(errorResponse);

        // then
        assertNotNull(json);
        assertTrue(json.contains("ErrorProcess123"));
        assertTrue(json.contains("2024-06-12"));
        assertTrue(json.contains("Error1"));
        assertTrue(json.contains("Error2"));
    }

    @Test
    void deserializeFromJson_shouldDeserializeCorrectly() throws Exception {
        // given
        String json = "{\"processKey\":\"ErrorProcess123\",\"createdAt\":\"2024-06-12\",\"errors\":[\"Error1\",\"Error2\"]}";

        // when
        TagErrorResponse errorResponse = mapper.readValue(json, TagErrorResponse.class);

        // then
        assertNotNull(errorResponse);
        assertEquals("ErrorProcess123", errorResponse.getProcessKey());
        assertEquals("2024-06-12", errorResponse.getCreatedAt());
        assertEquals(2, errorResponse.getErrors().size());
        assertTrue(errorResponse.getErrors().contains("Error1"));
        assertTrue(errorResponse.getErrors().contains("Error2"));
    }
}
