package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class TagResponse {
    private List<CommercialEstablishment> commercialEstablishments;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommercialEstablishment {
        private String key;
        private String documentType;
        private String documentNumber;
        private boolean enabled;
        private List<String> paymentSchemes;
        private TagBankAccount bankAccount;
        private String processKey;
        private String createdAt;
        private String updatedAt;
    }

}
