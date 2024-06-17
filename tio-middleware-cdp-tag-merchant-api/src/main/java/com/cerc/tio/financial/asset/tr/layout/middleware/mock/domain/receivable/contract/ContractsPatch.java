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
public class ContractsPatch {
    private String key = "e07d6c53-7dc0-429d-a1da-d76f955f6dfd";
    private String isCanceled = "false";
    private String contractDueDate = "2020-10-13";
    private String contractUniqueIdentifier = "12100000123";
    private String warrantyAmount = "8000000";
    private String balanceDue = "8000000";
    private String percentageValue = "0";
    private TagBankAccount bankAccount = null;
    private List<ContractPatchSpecifications> contractSpecifications = null;
}

