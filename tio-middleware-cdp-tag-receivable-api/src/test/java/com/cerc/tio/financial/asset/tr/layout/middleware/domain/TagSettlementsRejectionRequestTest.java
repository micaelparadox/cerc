package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TagSettlementsRejectionRequestTest {
	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testValidTagSettlementsRejectionRequest() {
		TagSettlementRejection settlementRejection = TagSettlementRejection.builder().key("validKey")
				.reasonDetails("validReason").build();

		Set<ConstraintViolation<TagSettlementRejection>> violations = validator.validate(settlementRejection);
		assertTrue(violations.isEmpty(), "There should be no violations for a valid setup");
	}

	@Test
	void testInvalidTagSettlementsRejectionRequest() {
		TagSettlementRejection settlementRejection = TagSettlementRejection.builder().build();

		Set<ConstraintViolation<TagSettlementRejection>> violations = validator.validate(settlementRejection);
		assertFalse(violations.isEmpty(), "Violations should be present for invalid setup");
	}
}
