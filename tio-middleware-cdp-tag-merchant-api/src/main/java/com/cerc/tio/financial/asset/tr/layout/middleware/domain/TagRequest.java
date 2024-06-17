package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.cerc.tio.libcdpcommon.validation.ValidatableItem;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocument;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocumentType;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CNPJ;
import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CPF;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagRequest extends ValidatableItem {
    private List<CommercialEstablishment> commercialEstablishments;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class CommercialEstablishment extends ValidatableItem {
        @NotBlank
        @ValidateDocumentType(type = {CNPJ, CPF}, message = "invalid document type")
        private String documentType;
        @NotBlank(message = "document number is required")
        @ValidateDocument(type = {CNPJ, CPF}, message = "invalid document number")
        private String documentNumber;
        private Boolean enabled;
        private List<@Size(min = 3, max = 3, message = "invalid payment scheme code length") String> paymentSchemes;
        private TagBankAccount bankAccount;

        @Override
        public void validate(Validator validator) {
            super.validate(validator);
            super.validateChild(validator, bankAccount);
        }
    }

    @Override
    public void validate(Validator validator) {
        super.validateChildren(validator, commercialEstablishments);
    }
}
