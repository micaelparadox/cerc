package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.contract;

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
public class PreExistingContractsResponse {
    private String processKey = null;
    private String assetHolderDocument = null;
    private List<String> paymentSchemes = null;
    private Boolean hasErrors = true;
    private List errors = null;
}
