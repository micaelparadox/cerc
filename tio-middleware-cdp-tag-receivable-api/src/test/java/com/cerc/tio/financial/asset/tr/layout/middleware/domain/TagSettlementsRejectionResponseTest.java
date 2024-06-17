package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagResponseItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagSettlementsRejectionResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serializeToJson_shouldSerializeCorrectly() throws Exception {
        // given
        TagResponseItem responseItem = TagResponseItem.builder()
                .key("SettleKey001")
                .reference("SettleRef001")
                .build();

        TagSettlementsRejectionResponse response = TagSettlementsRejectionResponse.builder()
                .processKey("Process123")
                .createdAt("2024-06-12")
                .settlements(Collections.singletonList(responseItem))
                .errors(List.of("Error1", "Error2"))
                .build();

        // when
        String json = mapper.writeValueAsString(response);

        // then
        assertNotNull(json);
        assertTrue(json.contains("Process123"));
        assertTrue(json.contains("2024-06-12"));
        assertTrue(json.contains("SettleKey001"));
        assertTrue(json.contains("Error1"));
        assertTrue(json.contains("Error2"));
    }

    @Test
    void deserializeFromJson_shouldDeserializeCorrectly() throws Exception {
        // given
        String json = "{\"processKey\":\"Process123\",\"createdAt\":\"2024-06-12\",\"settlements\":[{\"key\":\"SettleKey001\",\"reference\":\"SettleRef001\"}],\"errors\":[\"Error1\",\"Error2\"]}";

        // when
        TagSettlementsRejectionResponse response = mapper.readValue(json, TagSettlementsRejectionResponse.class);

        // then
        assertNotNull(response);
        assertEquals("Process123", response.getProcessKey());
        assertEquals("2024-06-12", response.getCreatedAt());
        assertEquals(1, response.getSettlements().size());
        assertEquals("SettleKey001", response.getSettlements().get(0).getKey());
        assertEquals(2, response.getErrors().size());
        assertTrue(response.getErrors().contains("Error1"));
    }
}
