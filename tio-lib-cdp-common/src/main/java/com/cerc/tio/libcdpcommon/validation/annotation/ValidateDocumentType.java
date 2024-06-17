package com.cerc.tio.libcdpcommon.validation.annotation;

import com.cerc.tio.libcdpcommon.validation.constraint.DocumentTypeValidator;
import com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DocumentTypeValidator.class})
public @interface ValidateDocumentType {
    String message() default "document type is invalid.";

    String[] exclude() default "";

    DocumentValidationType[] type();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
