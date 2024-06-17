package com.cerc.tio.libcdpcommon.validation.constraint;


import com.cerc.tio.libcdpcommon.validation.annotation.ValidateEnum;
import com.cerc.tio.libcdpcommon.validation.enumeration.ValuedEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {
    private ValidateEnum annotation;

    @Override
    public void initialize(ValidateEnum annotation) {
        this.annotation = annotation;
    }

    @Override
    @SuppressWarnings("squid:S1066")
    public boolean isValid(String valueForValidation, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;

        Enum<?>[] enumValues = this.annotation.type()
            .getEnumConstants();

        if (enumValues != null && valueForValidation != null) {
            for (Enum<?> enumValue : enumValues) {
                if (enumValue instanceof ValuedEnum valuable) {
                    if (valueForValidation.equalsIgnoreCase(valuable.getValue()) ||
                        valueForValidation.equalsIgnoreCase(enumValue.name())) {
                        result = true;
                        break;
                    }
                }
            }
        } else if (valueForValidation == null) {
            result = true;
        }

        return result;
    }
}