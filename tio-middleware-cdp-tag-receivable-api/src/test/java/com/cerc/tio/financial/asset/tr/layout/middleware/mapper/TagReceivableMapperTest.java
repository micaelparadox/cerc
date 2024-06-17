package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesResponse;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;

class TagReceivableMapperTest {

	@Test
	void testMapToBKTransactionRequest() {
		List<TagReceivablesRequest.TagReceivable> receivables = new ArrayList<>();
		String companyDocument = "12345678901234";
		String companyId = "companyId";
		String correlationId = "correlationId";

		TagReceivablesRequest.TagReceivable receivable = TagReceivablesRequest.TagReceivable.builder()
				.reference("ref123").dueDate("2024-06-12").originalAssetHolder("assetHolder").paymentScheme("SCH")
				.amount(BigInteger.valueOf(1000)).build();

		receivables.add(receivable);

		List<BKTransactionRequest> result = TagReceivableMapper.mapToBKTransactionRequest(receivables, companyDocument,
				companyId, correlationId);

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
	}

	@Test
	void testMapToBKTransactionRequestEmpty() {
		List<TagReceivablesRequest.TagReceivable> receivables = Collections.emptyList();
		String companyDocument = "12345678901234";
		String companyId = "companyId";
		String correlationId = "correlationId";

		List<BKTransactionRequest> result = TagReceivableMapper.mapToBKTransactionRequest(receivables, companyDocument,
				companyId, correlationId);

		assertTrue(result.isEmpty());
	}

	@Test
	void testMapResponse() {
		List<TagReceivablesRequest.TagReceivable> validRequests = new ArrayList<>();
		String createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();
		String correlationId = "correlationId";
		String companyDocument = "12345678901234";

		TagReceivablesRequest.TagReceivable receivable = TagReceivablesRequest.TagReceivable.builder()
				.reference("ref123").dueDate("2024-06-12").originalAssetHolder("assetHolder").paymentScheme("SCH")
				.amount(BigInteger.valueOf(1000)).build();

		validRequests.add(receivable);

		TagReceivablesResponse result = TagReceivableMapper.mapResponse(validRequests, createdAt, correlationId,
				companyDocument);

		assertEquals(correlationId, result.getProcessKey());
		assertFalse(result.getReceivables().isEmpty());
		assertEquals(1, result.getReceivables().size());
	}
}
