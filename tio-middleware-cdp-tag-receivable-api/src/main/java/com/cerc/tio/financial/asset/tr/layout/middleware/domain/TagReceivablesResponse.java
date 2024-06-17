package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagReceivablesResponse {
    private String processKey;
    private String createdAt;
    private List<TagReceivableResponse> receivables;
    private List<String> errors;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagReceivableResponse {

        private String reference;
        private String dueDate;
        private String originalAssetHolderDocumentType;
        private String originalAssetHolder;
        private String paymentScheme;
        private String key;
        private List<TagReceivableSettlementResponse> settlements;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TagReceivableSettlementResponse {

            private String reference;
            private String key;
            private String assetHolder;

        }


    }

}