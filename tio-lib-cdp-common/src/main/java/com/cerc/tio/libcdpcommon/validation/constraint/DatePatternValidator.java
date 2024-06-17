package com.cerc.tio.libcdpcommon.validation.constraint;

import com.cerc.tio.libcdpcommon.validation.annotation.DatePattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DatePatternValidator implements ConstraintValidator<DatePattern, String> {
    private String datePattern;

    @Override
    public void initialize(DatePattern datePattern) {
        this.datePattern = datePattern.pattern();
    }

    @Override
    public boolean isValid(String dateField, ConstraintValidatorContext cxt) {
        if (dateField == null) {
            return true;
        }
        try {
            LocalDate.parse(dateField, DateTimeFormatter.ofPattern(datePattern));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}