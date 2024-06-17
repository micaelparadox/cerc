package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagReceivablesResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serializeToJson_shouldSerializeCorrectly() throws Exception {
        // given
        TagReceivablesResponse.TagReceivableResponse.TagReceivableSettlementResponse settlementResponse = TagReceivablesResponse.TagReceivableResponse.TagReceivableSettlementResponse.builder()
                .reference("SettleRef001")
                .key("Key001")
                .assetHolder("Holder001")
                .build();

        TagReceivablesResponse.TagReceivableResponse receivableResponse = TagReceivablesResponse.TagReceivableResponse.builder()
                .reference("ReceivableRef001")
                .dueDate("2024-12-31")
                .originalAssetHolderDocumentType("CNPJ")
                .originalAssetHolder("12345678000199")
                .paymentScheme("ACH")
                .key("ReceivableKey001")
                .settlements(Collections.singletonList(settlementResponse))
                .build();

        TagReceivablesResponse response = TagReceivablesResponse.builder()
                .processKey("Process123")
                .createdAt("2024-06-12")
                .receivables(Collections.singletonList(receivableResponse))
                .errors(List.of("Error1"))
                .build();

        // when
        String json = mapper.writeValueAsString(response);

        // then
        assertNotNull(json);
        assertTrue(json.contains("Process123"));
        assertTrue(json.contains("2024-06-12"));
        assertTrue(json.contains("ReceivableRef001"));
        assertTrue(json.contains("Key001"));
    }

    @Test
    void deserializeFromJson_shouldDeserializeCorrectly() throws Exception {
        // given
        String json = "{\"processKey\":\"Process123\",\"createdAt\":\"2024-06-12\",\"receivables\":[{\"reference\":\"ReceivableRef001\",\"dueDate\":\"2024-12-31\",\"originalAssetHolderDocumentType\":\"CNPJ\",\"originalAssetHolder\":\"12345678000199\",\"paymentScheme\":\"ACH\",\"key\":\"ReceivableKey001\",\"settlements\":[{\"reference\":\"SettleRef001\",\"key\":\"Key001\",\"assetHolder\":\"Holder001\"}]}],\"errors\":[\"Error1\"]}";

        // when
        TagReceivablesResponse response = mapper.readValue(json, TagReceivablesResponse.class);

        // then
        assertNotNull(response);
        assertEquals("Process123", response.getProcessKey());
        assertEquals("2024-06-12", response.getCreatedAt());
        assertEquals(1, response.getReceivables().size());
        assertEquals("ReceivableRef001", response.getReceivables().get(0).getReference());
        assertEquals(1, response.getErrors().size());
        assertTrue(response.getErrors().contains("Error1"));
    }
}
