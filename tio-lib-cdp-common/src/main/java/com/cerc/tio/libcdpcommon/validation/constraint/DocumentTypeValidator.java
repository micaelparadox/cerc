package com.cerc.tio.libcdpcommon.validation.constraint;

import com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocumentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class DocumentTypeValidator implements ConstraintValidator<ValidateDocumentType, String> {

    private Set<DocumentValidationType> type;

    @Override
    public void initialize(ValidateDocumentType constraintAnnotation) {
        this.type = Set.of(constraintAnnotation.type());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty() || validateValue(value);
    }

    public boolean validateValue(String value) {
        DocumentValidationType documentValidationType = DocumentValidationType.fromString(value);
        return documentValidationType != null && this.type.contains(documentValidationType);
    }
}
