package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.cerc.tio.libcdpcommon.validation.ValidatableItem;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocument;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocumentType;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateEnum;
import com.cerc.tio.libcdpcommon.validation.enumeration.AccountType;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CNPJ;
import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CPF;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagBankAccount extends ValidatableItem {
    @Size(min = 1, max = 4, message = "invalid branch length")
    private String branch;
    @Size(min = 1, max = 18, message = "invalid account length")
    private String account;
    @Size(min = 1, max = 2, message = "invalid account digit length")
    private String accountDigit;
    @ValidateEnum(type = AccountType.class, message = "invalid account type")
    private String accountType;
    @Size(min = 8, max = 8, message = "invalid ispb length")
    private String ispb;
    @ValidateDocumentType(type = {CNPJ, CPF}, message = "invalid bank account document type")
    private String documentType;
    @ValidateDocument(type = {CNPJ, CPF}, message = "invalid bank account document number")
    private String documentNumber;
}