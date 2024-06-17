package com.cerc.tio.libcdpcommon.validation.constraint;

import com.cerc.tio.libcdpcommon.validation.annotation.DatePattern;
import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatePatternValidatorTest {
    DatePatternValidator underTest;

    @BeforeEach
    void setUp() {
        DatePattern datePattern = buildDatePattern();
        underTest = new DatePatternValidator();
        underTest.initialize(datePattern);
    }

    @Test
    void isValid_shouldReturnTrue_whenNull() {
        assertTrue(underTest.isValid(null, null));
    }

    @Test
    void isValid_shouldReturnTrue_whenCorrectFormatAndValidDate() {
        assertTrue(underTest.isValid("2022-01-01", null));
    }

    @Test
    void isValid_shouldReturnTrue_whenCorrectFormatAndFutureDate() {
        String futureDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().plusDays(1));
        assertTrue(underTest.isValid(futureDate, null));
    }

    @Test
    void isValid_shouldReturnFalse_whenIncorrectFormat() {
        assertFalse(underTest.isValid("01-01-2022", null));
    }

    @Test
    void isValid_shouldReturnFalse_whenInvalidDate() {
        assertFalse(underTest.isValid("2022-01-32", null));
    }

    private DatePattern buildDatePattern() {
        return new DatePattern() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return "Invalid date format";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String pattern() {
                return "yyyy-MM-dd";
            }
        };
    }

}