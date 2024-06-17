package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagBankAccount;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagSettlement;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;

class TagSettlementMapperTest {

	@Test
	void testMapFromSettlements() {
		List<TagSettlement> settlements = new ArrayList<>();
		String companyDocument = "12345678901234";
		String companyId = "companyId";
		String correlationId = "correlationId";

		TagBankAccount bankAccount = TagBankAccount.builder().branch("1234").account("567890").accountDigit("0")
				.accountType("CC").ispb("12345678").documentNumber("12345678901234").build();

		TagSettlement settlement = TagSettlement.builder().reference("ref123").assetHolder("assetHolder")
				.settlementDate("2024-06-12").settlementObligationDate("2024-12-31").paymentScheme("SCH")
				.amount(BigInteger.valueOf(1000)).bankAccount(bankAccount).build();

		settlements.add(settlement);

		List<BKTransactionRequest> result = TagSettlementMapper.mapFrom(settlements, companyDocument, companyId,
				correlationId);

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
	}

	@Test
	void testMapFromEmptySettlements() {
		List<TagSettlement> settlements = Collections.emptyList();
		String companyDocument = "12345678901234";
		String companyId = "companyId";
		String correlationId = "correlationId";

		List<BKTransactionRequest> result = TagSettlementMapper.mapFrom(settlements, companyDocument, companyId,
				correlationId);

		assertTrue(result.isEmpty());
	}

	@Test
	void testMapFromRequest() {
		List<TagSettlement> settlements = new ArrayList<>();
		TagSettlementsRequest request = TagSettlementsRequest.builder().settlements(settlements).build();

		String correlationId = "correlationId";
		String companyDocument = "12345678901234";
		List<String> errors = new ArrayList<>();

		TagBankAccount bankAccount = TagBankAccount.builder().branch("1234").account("567890").accountDigit("0")
				.accountType("CC").ispb("12345678").documentNumber("12345678901234").build();

		TagSettlement settlement = TagSettlement.builder().reference("ref123").assetHolder("assetHolder")
				.settlementDate("2024-06-12").settlementObligationDate("2024-12-31").paymentScheme("SCH")
				.amount(BigInteger.valueOf(1000)).bankAccount(bankAccount).build();

		settlements.add(settlement);

		List<BKTransactionRequest> validSettlementList = TagSettlementMapper.mapFrom(settlements, companyDocument,
				"companyId", correlationId);

		TagSettlementsResponse result = TagSettlementMapper.mapFrom(validSettlementList, errors, correlationId,
				companyDocument);

		assertEquals(correlationId, result.getProcessKey());
		assertFalse(result.getSettlements().isEmpty());
		assertEquals(1, result.getSettlements().size());
		assertTrue(errors.isEmpty());
	}
}
