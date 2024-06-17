package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagSettlement;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TagSettlementsRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testValidTagSettlementsRequest() {
		TagSettlement settlement = TagSettlement.builder().reference("ref123").assetHolder("12345678000195")
				.amount(BigInteger.valueOf(1000)).settlementDate("2023-06-12").build();

		TagSettlementsRequest request = TagSettlementsRequest.builder().idempotencyKey("uniqueKey123")
				.settlements(List.of(settlement)).build();

		assertDoesNotThrow(() -> request.validate(validator));
	}

}
