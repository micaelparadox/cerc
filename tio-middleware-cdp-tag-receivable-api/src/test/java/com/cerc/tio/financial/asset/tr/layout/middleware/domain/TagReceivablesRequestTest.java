package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesRequest.TagReceivable;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TagReceivablesRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testValidTagReceivableRequest() {
		TagReceivable receivable = TagReceivable.builder().reference("ref123").dueDate("2023-06-12")
				.originalAssetHolderDocumentType("CNPJ").originalAssetHolder("12345678000195").paymentScheme("SCH")
				.amount(BigInteger.valueOf(1000)).prePaidAmount(BigInteger.valueOf(500)).build();

		TagReceivablesRequest request = TagReceivablesRequest.builder().processReference("uniqueProcessRef")
				.receivables(List.of(receivable)).build();

		assertDoesNotThrow(() -> request.validate(validator));
	}

	@Test
	void testGetReceivableKey() throws NoSuchAlgorithmException {
		TagReceivable receivable = TagReceivable.builder().reference("ref123").dueDate("2023-06-12")
				.originalAssetHolderDocumentType("CNPJ").originalAssetHolder("12345678000195").paymentScheme("SCH")
				.amount(BigInteger.valueOf(1000)).prePaidAmount(BigInteger.valueOf(500)).build();

		String receivableKey = receivable.getReceivableKey("12345678000195");
		assertEquals(36, receivableKey.length());
	}
}
