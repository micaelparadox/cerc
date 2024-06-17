package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsRequest.TagAdvancement;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsRequest.TagAdvancement.TagAdvancedReceivables;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TagAdvancementsRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testValidTagAdvancementRequest() {
		TagAdvancedReceivables receivables = TagAdvancedReceivables.builder().paymentScheme("SCH")
				.settlementObligationDate("2023-06-12").advancedAmount(BigInteger.valueOf(1000)).build();

		TagAdvancement advancement = TagAdvancement.builder().reference("ref123").assetHolderDocumentType("CNPJ")
				.assetHolder("12345678000195").operationValue(BigInteger.valueOf(1000))
				.operationExpectedSettlementDate("2023-06-12").advancedReceivables(List.of(receivables)).build();

		TagAdvancementsRequest request = TagAdvancementsRequest.builder().idempotencyKey("uniqueKey123")
				.advancements(List.of(advancement)).build();

		assertDoesNotThrow(() -> request.validate(validator));
	}

	@Test
	void testGetAdvancementContractId() throws NoSuchAlgorithmException {
		TagAdvancement advancement = TagAdvancement.builder().reference("ref123").assetHolderDocumentType("CNPJ")
				.assetHolder("12345678000195").operationValue(BigInteger.valueOf(1000))
				.operationExpectedSettlementDate("2023-06-12").advancedReceivables(List.of()).build();

		String contractId = advancement.getAdvancementContractId("12345678000195");
		assertEquals(36, contractId.length());
	}
}
