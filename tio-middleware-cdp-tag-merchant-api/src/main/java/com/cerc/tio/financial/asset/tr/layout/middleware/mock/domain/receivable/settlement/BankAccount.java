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
//TODO: Essa classe pode ser removida e utilizada a TagBankAccount
public class BankAccount {
    private String branch = "3232";
    private String account = "00121";
    private String accountDigit = "X3";
    private String accountType = "CC,CD,CG,CI, PG,PP";
    private String ispb = "99999999";
    private String documentType = "CPF, CNPJ";
    private String documentNumber = "99999999999999";

}

