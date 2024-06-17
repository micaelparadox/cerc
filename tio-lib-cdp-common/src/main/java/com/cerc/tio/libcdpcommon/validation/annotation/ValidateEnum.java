package com.cerc.tio.libcdpcommon.validation.annotation;

import com.cerc.tio.libcdpcommon.validation.constraint.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { EnumValidator.class })
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEnum {
    String message() default "Invalid value. This is not permitted.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @SuppressWarnings("squid:S1452")
    Class<? extends Enum<?>> type();
}
