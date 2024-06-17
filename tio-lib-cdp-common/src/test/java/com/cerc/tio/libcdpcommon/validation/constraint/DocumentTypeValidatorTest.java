package com.cerc.tio.libcdpcommon.validation.constraint;

import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocumentType;
import com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CNPJ;
import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CPF;

@ExtendWith(MockitoExtension.class)
class DocumentTypeValidatorTest {
    @Mock
    ValidateDocumentType validateDocument;

    DocumentTypeValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new DocumentTypeValidator();
        BDDMockito.given(validateDocument.type()).willReturn(new DocumentValidationType[]{ CPF, CNPJ });
        underTest.initialize(validateDocument);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "CPF", "CNPJ", "" })
    void isValid_Should_return_true_When_value_is_valid(String value) {
        // When
        boolean isValid = underTest.isValid(value, null);

        // Then
        Assertions.assertTrue(isValid, String.format("value %s is valid", value));
    }

    @ParameterizedTest
    @ValueSource(strings = { "INVALID", " " })
    void isValid_Should_return_false_When_value_is_invalid(String value) {
        // When
        boolean isValid = underTest.isValid(value, null);

        // Then
        Assertions.assertFalse(isValid, String.format("value %s is valid", value));
    }

}