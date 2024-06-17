package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.reconciliation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmResponse {
    private String reconciliationKey = null;
    private String reconciliationStatus = null;
    private String createdAt = null;
    private String processKey = null;
    private String lastUpdated = null;
    private String extractionReferenceDate = null;
}
