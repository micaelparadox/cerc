package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsResponse;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.Contract;

class TagAdvancementMapperTest {

	@Test
	void testMapFromAdvancements() throws NoSuchAlgorithmException {
		List<TagAdvancementsRequest.TagAdvancement> advancements = new ArrayList<>();
		List<String> errors = new ArrayList<>();
		String companyDocument = "12345678901234";
		String companyId = "companyId";
		String correlationId = "correlationId";

		TagAdvancementsRequest.TagAdvancement.TagAdvancedReceivables receivable = TagAdvancementsRequest.TagAdvancement.TagAdvancedReceivables
				.builder().paymentScheme("SCH").settlementObligationDate("2024-06-12")
				.advancedAmount(BigInteger.valueOf(1000)).build();

		TagAdvancementsRequest.TagAdvancement advancement = TagAdvancementsRequest.TagAdvancement.builder()
				.reference("ref123").assetHolder("assetHolder").operationValue(BigInteger.valueOf(1000))
				.operationExpectedSettlementDate("2024-12-31")
				.advancedReceivables(Collections.singletonList(receivable)).build();

		advancements.add(advancement);

		List<BKTransactionRequest> result = TagAdvancementMapper.mapFrom(advancements, errors, companyDocument,
				companyId, correlationId);

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertTrue(errors.isEmpty());
	}

	@Test
	void testMapFromEmptyAdvancements() {
		List<TagAdvancementsRequest.TagAdvancement> advancements = Collections.emptyList();
		List<String> errors = new ArrayList<>();
		String companyDocument = "12345678901234";
		String companyId = "companyId";
		String correlationId = "correlationId";

		List<BKTransactionRequest> result = TagAdvancementMapper.mapFrom(advancements, errors, companyDocument,
				companyId, correlationId);

		assertTrue(result.isEmpty());
		assertTrue(errors.isEmpty());
	}

	@Test
	void testMapFromBKTransactionRequests() {
		List<BKTransactionRequest> validAdvancementsList = new ArrayList<>();
		List<String> errors = new ArrayList<>();
		String correlationId = "correlationId";
		String companyDocument = "12345678901234";

		BKTransactionRequest transactionRequest = BKTransactionRequest.builder()
				.contract(Contract.builder().contractId("contractId").reference("reference").build()).build();

		validAdvancementsList.add(transactionRequest);

		TagAdvancementsResponse result = TagAdvancementMapper.mapFrom(validAdvancementsList, errors, correlationId,
				companyDocument);

		assertEquals(correlationId, result.getProcessKey());
		assertFalse(result.getAdvancements().isEmpty());
		assertEquals(1, result.getAdvancements().size());
		assertTrue(errors.isEmpty());
	}
}
