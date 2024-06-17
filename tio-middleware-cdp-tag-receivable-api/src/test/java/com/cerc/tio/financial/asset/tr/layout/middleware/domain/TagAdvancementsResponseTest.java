package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagResponseItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagAdvancementsResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serializeToJson_shouldSerializeCorrectly() throws Exception {
        // given
        TagAdvancementsResponse response = TagAdvancementsResponse.builder()
                .processKey("Process123")
                .createdAt("2024-06-12")
                .advancements(List.of(new TagResponseItem("Adv001", "StatusOK")))
                .errors(List.of("Error1", "Error2"))
                .build();

        // when
        String json = mapper.writeValueAsString(response);

        // then
        assertNotNull(json);
        assertTrue(json.contains("Process123"));
        assertTrue(json.contains("2024-06-12"));
        assertTrue(json.contains("Adv001"));
        assertTrue(json.contains("Error1"));
    }

    @Test
    void deserializeFromJson_shouldDeserializeCorrectly() throws Exception {
        // given
        String json = "{\"processKey\":\"Process123\",\"createdAt\":\"2024-06-12\",\"advancements\":[{\"key\":\"Adv001\",\"reference\":\"StatusOK\"}],\"errors\":[\"Error1\",\"Error2\"]}";

        // when
        TagAdvancementsResponse response = mapper.readValue(json, TagAdvancementsResponse.class);

        // then
        assertNotNull(response);
        assertEquals("Process123", response.getProcessKey());
        assertEquals("2024-06-12", response.getCreatedAt());
        assertEquals(1, response.getAdvancements().size());
        assertEquals("Adv001", response.getAdvancements().get(0).getKey());
        assertEquals(2, response.getErrors().size());
        assertTrue(response.getErrors().contains("Error1"));
    }
}
