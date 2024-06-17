package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionDomainType;
import com.cerc.tio.libcdpcommon.util.UUIDv5Util;
import com.cerc.tio.libcdpcommon.validation.ValidatableItem;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocument;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocumentType;
import jakarta.validation.Validator;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
public class TagAdvancementsRequest extends ValidatableItem {
    private String idempotencyKey;

    @NotEmpty(message = "advancements list is required")
    private List<TagAdvancement> advancements;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class TagAdvancement extends ValidatableItem {

        @NotBlank(message = "reference is required")
        private String reference;

        @NotBlank(message = "asset holder document type is required")
        @ValidateDocumentType(type = {CNPJ, CPF}, message = "invalid asset holder document type")
        private String assetHolderDocumentType;

        @NotBlank(message = "asset holder document number is required")
        @ValidateDocument(type = {CNPJ, CPF}, message = "invalid asset holder document number")
        private String assetHolder;

        @NotNull(message = "operation value is required")
        @Min(value = 0, message = "operation value must be non-negative")
        private BigInteger operationValue;

        @NotBlank(message = "operation expected settlement date is required")
        private String operationExpectedSettlementDate;

        @NotEmpty(message = "advanced receivables list is required")
        private List<TagAdvancedReceivables> advancedReceivables;

        public String getAdvancementContractId(String originalRequesterHash) throws NoSuchAlgorithmException {
            String namespace = UUID.nameUUIDFromBytes(BKTransactionDomainType.FINANCIAL_ASSET.getDomain().getBytes()).toString();
            String contractHash = String.format(
                    "%s_%s_%s",
                    originalRequesterHash,
                    assetHolder,
                    reference
            );
            return UUIDv5Util.generateUUIDv5(namespace, contractHash).toString();
        }

        @Override
        public void validate(Validator validator) {
            super.validate(validator);
            super.validateChildren(validator, advancedReceivables);
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @EqualsAndHashCode(callSuper = true)
        public static class TagAdvancedReceivables extends ValidatableItem {
            @NotBlank(message = "payment scheme is required")
            @Size(min = 3, max = 3, message = "invalid payment scheme code length")
            private String paymentScheme;
            @NotBlank(message = "settlement obligation date required")
            private String settlementObligationDate;

            @NotNull(message = "advanced amount is required")
            @Min(value=0, message = "advanced amount must be a positive number")
            private BigInteger advancedAmount;
        }
    }

    @Override
    public void validate(Validator validator) {
        super.validate(validator);
        super.validateChildren(validator, advancements);
    }
}