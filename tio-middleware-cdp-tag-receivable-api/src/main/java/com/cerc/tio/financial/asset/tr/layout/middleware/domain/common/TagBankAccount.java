package com.cerc.tio.financial.asset.tr.layout.middleware.domain.common;

import com.cerc.tio.libcdpcommon.validation.ValidatableItem;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocument;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocumentType;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateEnum;
import com.cerc.tio.libcdpcommon.validation.enumeration.AccountType;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "branch is required")
    @Size(min = 1, max = 4, message = "invalid branch length")
    private String branch;

    @NotNull(message = "account is required")
    @Size(min = 1, max = 18, message = "invalid account length")
    private String account;

    @NotNull(message = "account digit is required")
    @Size(min = 1, max = 2, message = "invalid account digit length")
    private String accountDigit;

    @NotNull(message = "account type is required")
    @ValidateEnum(type = AccountType.class, message = "invalid account type")
    private String accountType;

    @NotNull(message = "ispb is required")
    @Size(min = 8, max = 8, message = "invalid ispb length")
    private String ispb;

    @NotNull(message = "document type is required")
    @ValidateDocumentType(type = {CNPJ, CPF}, message = "invalid bank account document type")
    private String documentType;

    @NotNull(message = "document number is required")
    @ValidateDocument(type = {CNPJ, CPF}, message = "invalid bank account document number")
    private String documentNumber;
}