package com.cerc.tio.libcdpcommon.validation.constraint;

import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocument;
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
class DocumentValidatorTest {

    @Mock
    ValidateDocument validateDocument;

    DocumentValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new DocumentValidator();
        BDDMockito.given(validateDocument.exclude()).willReturn(new String[]{});
        BDDMockito.given(validateDocument.type()).willReturn(new DocumentValidationType[]{ CPF, CNPJ });
        underTest.initialize(validateDocument);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "16110626082", "16204674000148", ""})
    void isValid_Should_return_true_When_document_is_valid(String document) {
        // When
        boolean isValid = underTest.isValid(document, null);

        // Then
        Assertions.assertTrue(isValid, String.format("Document %s is valid", document));
    }

    @ParameterizedTest
    @ValueSource(strings = { "1234567890", " ",
        "00000000000000", "11111111111", "22222222222", "33333333333", "44444444444", "55555555555",
        "66666666666", "77777777777", "88888888888", "99999999999"
    })
    void isValid_Should_return_false_When_document_is_invalid(String document) {
        // When
        boolean isValid = underTest.isValid(document, null);

        // Then
        Assertions.assertFalse(isValid, String.format("Document %s is valid", document));
    }

}