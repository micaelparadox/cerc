package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.contract;

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
public class ContractPostSpecifications {
    private String expectedSettlementDate = "2021-11-01";
    private String originalAssetHolder = "99999999999999";
    private String receivableDebtor = "88888888888888";
    private String paymentScheme = "VCC";
    private String effectValue = "8000000";
    private String initialExpectedSettlementDate = "2021-11-01";
    private String finalExpectedSettlementDate = "2021-11-05";
}