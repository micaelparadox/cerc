package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TagRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validate_Should_add_no_errors_When_request_is_valid() {
        // given
        var request = buildValidRequest();

        // when
        request.validate(validator);

        // then
        assertTrue(request.isValid());
        assertTrue(request.getErrors().isEmpty());
    }

    // TODO: add error validation test

    private TagRequest buildValidRequest() {
        return TagRequest.builder()
            .commercialEstablishments(
                List.of(
                    TagRequest.CommercialEstablishment.builder()
                        .paymentSchemes(List.of("VCC"))
                        .documentType("CPF")
                        .documentNumber("16110626082")
                        .bankAccount(
                            TagBankAccount.builder()
                                .branch("1234")
                                .account("12346789")
                                .accountDigit("X1")
                                .accountType("CC")
                                .ispb("12345678")
                                .documentType("CPF")
                                .documentNumber("16110626082")
                                .build()
                        )
                        .build()
                )
            )
            .build();
    }
}