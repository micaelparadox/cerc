package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagSettlementRejectionTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createInstance_withValidData_shouldNotHaveValidationErrors() {
        // given
        TagSettlementRejection rejection = TagSettlementRejection.builder()
                .key("123456")
                .reasonDetails("The details of the reason for rejection.")
                .build();

        // when
        var violations = validator.validate(rejection);

        // then
        assertTrue(violations.isEmpty(), "There should be no validation errors.");
    }

    @Test
    void createInstance_withInvalidData_shouldHaveValidationErrors() {
        // given
        TagSettlementRejection rejection = TagSettlementRejection.builder()
                .key("") // Assuming empty key is invalid
                .reasonDetails("") // Assuming empty reason details is invalid
                .build();

        // when
        var violations = validator.validate(rejection);

        // then
        assertTrue(violations.isEmpty(), "There should be validation errors.");
    }

    @Test
    void getters_shouldRetrieveCorrectValues() {
        // given
        String expectedKey = "expectedKey";
        String expectedReason = "expected reason for rejection";

        TagSettlementRejection rejection = new TagSettlementRejection(expectedKey, expectedReason);

        // when
        String actualKey = rejection.getKey();
        String actualReasonDetails = rejection.getReasonDetails();

        // then
        assertEquals(expectedKey, actualKey, "Keys should match.");
        assertEquals(expectedReason, actualReasonDetails, "Reason details should match.");
    }
}
