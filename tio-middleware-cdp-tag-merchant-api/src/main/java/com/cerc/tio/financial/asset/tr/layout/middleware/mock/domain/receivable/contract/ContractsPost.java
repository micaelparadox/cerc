package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.contract;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagBankAccount;
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
public class ContractsPost {
    private String reference = "CG_15";
    private String contractDueDate = "2020-10-13";
    private String assetHolderDocumentType = "CNPJ";
    private String assetHolder = "99999999999999";
    private String contractUniqueIdentifier = "12100000123";
    private String signatureDate = "2020-10-13";
    private String effectType = "warranty";
    private String warrantyType = "fiduciary";
    private String warrantyAmount = "8000000";
    private String balanceDue = "8000000";
    private String divisionMethod = "fixedAmount";
    private String effectStrategy = "specific";
    private String percentageValue = "0";
    private TagBankAccount bankAccount = null;
    private List<ContractPostSpecifications> contractSpecifications = null;
}

