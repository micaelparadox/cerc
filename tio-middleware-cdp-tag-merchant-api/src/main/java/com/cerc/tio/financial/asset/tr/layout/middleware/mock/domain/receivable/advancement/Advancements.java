package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement;

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
public class Advancements {
    private String reference = "P_54636";
    private String assetHolderDocumentType = "CNPJ/CPF";
    private String assetHolder = "88888888888888";
    private Integer operationValue = 15000;
    private String operationExpectedSettlementDate = "2021-02-13";
    private List<AdvancedReceivables> advancedReceivables = null;
}

