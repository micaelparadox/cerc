package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagBankAccount;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagSettlement;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionDomainType;
import com.cerc.tio.libcdpcommon.util.UUIDv5Util;
import com.cerc.tio.libcdpcommon.validation.ValidatableItem;
import com.cerc.tio.libcdpcommon.validation.annotation.DatePattern;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocument;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocumentType;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CNPJ;
import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CPF;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagReceivablesRequest extends ValidatableItem {
    private String processReference;

    @Size(max = 400, message = "max length of receivables surpassed")
    private List<TagReceivable> receivables;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class TagReceivable extends ValidatableItem {

        private String reference;

        @NotNull(message = "due date is required")
        @DatePattern(message = "invalid due date")
        private String dueDate;

        @NotNull(message = "original asset holder document type is required")
        @ValidateDocumentType(type = {CNPJ, CPF}, message = "invalid original asset holder document type")
        private String originalAssetHolderDocumentType;

        @NotNull(message = "original asset holder document number is required")
        @ValidateDocument(type = {CNPJ, CPF}, message = "invalid original asset holder document number")
        private String originalAssetHolder;

        @NotNull(message = "payment scheme is required")
        @Size(min = 3, max = 3, message = "invalid payment scheme code length")
        private String paymentScheme;

        @NotNull
        @Min(value = 0, message = "amount must be greater than or equal to 0")
        private BigInteger amount;

        @Min(value = 0, message = "prepaid amount must be greater than or equal to 0")
        private BigInteger prePaidAmount;

        private TagBankAccount bankAccount;

        private List<TagSettlement> settlements;

        public String getReceivableKey(String payer) {
            // TODO: Define namespace in a more meaningful way
            String namespace = UUID.nameUUIDFromBytes(BKTransactionDomainType.FINANCIAL_ASSET.getDomain().getBytes()).toString();

            String commitmentHash = String.format(
                    "%s_%s_%s_%s_%s",
                    originalAssetHolder,
                    originalAssetHolder,
                    payer,
                    paymentScheme,
                    dueDate
            );

            String receivableKey = null;

            try {
                receivableKey = UUIDv5Util.generateUUIDv5(namespace, commitmentHash).toString();
            } catch (NoSuchAlgorithmException e) {
                log.error("Error generating receivable key", e);
            }

            return receivableKey;
        }

        @Override
        public void validate(Validator validator) {
            super.validate(validator);
            super.validateChild(validator, bankAccount);
            super.validateChildren(validator, settlements);
        }
    }

    @Override
    public void validate(Validator validator) {
        super.validate(validator);
        super.validateChildren(validator, receivables);
    }
}