package com.cerc.tio.libcdpcommon.validation.constraint;

import com.cerc.tio.libcdpcommon.validation.annotation.ValidateEnum;
import com.cerc.tio.libcdpcommon.validation.enumeration.AccountType;
import jakarta.validation.Payload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;

@ExtendWith(MockitoExtension.class)
class EnumValidatorTest {
    EnumValidator underTest;

    @BeforeEach
    void setUp() {
        ValidateEnum validateEnum = buildValidateEnum();
        underTest = new EnumValidator();
        underTest.initialize(validateEnum);
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void isValid_Should_return_true_When_account_type_is_valid(AccountType accountType) {
        // When
        boolean isValid = underTest.isValid(accountType.name(), null);

        // Then
        Assertions.assertTrue(isValid, String.format("Account type %s is valid", accountType.name()));
    }

    @Test
    void isValid_Should_return_true_When_account_type_is_null() {
        // When
        boolean isValid = underTest.isValid(null, null);

        // Then
        Assertions.assertTrue(isValid, "Account type null is valid");
    }

    @Test
    void isValid_Should_return_false_When_account_type_is_invalid() {
        // When
        boolean isValid = underTest.isValid("INVALID", null);

        // Then
        Assertions.assertFalse(isValid, "Account type INVALID is valid");
    }

    /**
     * Build a {@link ValidateEnum} object because mockito can´t mock the return of
     * {@link ValidateEnum#type()} method.
     */
    private ValidateEnum buildValidateEnum() {
        return new ValidateEnum() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return "Invalid value. This is not permitted.";
            }

            @Override
            public Class<?>[] groups
                () {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public Class<? extends Enum<?>> type() {
                return AccountType.class;
            }
        };
    }

}