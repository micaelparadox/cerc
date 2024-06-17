package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementRejection;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsRejectionRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsResponse;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;

class TagSettlementRejectionMapperTest {

	@Test
	void testMapFromSettlementsRejection() {
		List<TagSettlementRejection> settlementsRejection = new ArrayList<>();
		String companyId = "companyId";
		String correlationId = "correlationId";

		TagSettlementRejection settlementRejection = TagSettlementRejection.builder().key("key123").build();

		settlementsRejection.add(settlementRejection);

		List<BKTransactionRequest> result = TagSettlementRejectionMapper.mapFrom(settlementsRejection, companyId,
				correlationId);

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
	}

	@Test
	void testMapFromEmptySettlementsRejection() {
		List<TagSettlementRejection> settlementsRejection = Collections.emptyList();
		String companyId = "companyId";
		String correlationId = "correlationId";

		List<BKTransactionRequest> result = TagSettlementRejectionMapper.mapFrom(settlementsRejection, companyId,
				correlationId);

		assertTrue(result.isEmpty());
	}

	@Test
	void testMapFromRequest() {
		List<TagSettlementRejection> settlements = new ArrayList<>();
		TagSettlementsRejectionRequest request = TagSettlementsRejectionRequest.builder().settlements(settlements)
				.build();
		String createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();
		String correlationId = "correlationId";

		TagSettlementRejection settlementRejection = TagSettlementRejection.builder().key("key123").build();

		settlements.add(settlementRejection);

		TagSettlementsResponse result = TagSettlementRejectionMapper.mapFrom(request, createdAt, correlationId);

		assertEquals(correlationId, result.getProcessKey());
		assertFalse(result.getSettlements().isEmpty());
		assertEquals(1, result.getSettlements().size());
	}
}
