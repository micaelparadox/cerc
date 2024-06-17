package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement;

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
public class Settlements {
    private String reference = "L_1875";
    private String assetHolderDocumentType = "CPF,CNPJ";
    private String assetHolder = "99999999999999";
    private String originalAssetHolder = "99999999999999";
    private String settlementDate = "2020-02-20";
    private Long amount = 100l;
    private String settlementObligationDate = "2021-11-20";
    private String paymentScheme = "VCC";
    private BankAccount bankAccount = null;
}
