package com.cerc.tio.financial.asset.tr.layout.middleware.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagBankAccount;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagResponseItem;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagSettlement;
import com.cerc.tio.financial.asset.tr.layout.middleware.mapper.TagReceivableMapper;
import com.cerc.tio.financial.asset.tr.layout.middleware.mapper.TagSettlementMapper;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionDomainType;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;
import com.cerc.tio.libcdpcommon.exception.FailedBatchTransactionException;
import com.cerc.tio.libcdpcommon.pubsub.publisher.BookkeepingPublisher;
import com.cerc.tio.libcdpcommon.pubsub.publisher.exception.MessagePublishingException;
import com.cerc.tio.libcdpcommon.util.domain.ProcessingIds;

import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
class TioMiddlewareCdpTagReceivableApiServiceTest {

	@Mock
	private BookkeepingPublisher bookkeepingPublisher;
	@Mock
	private Validator validator;

	private MockedStatic<TagReceivableMapper> tagReceivableMapperMockedStatic;
	private MockedStatic<TagSettlementMapper> tagSettlementMapperMockedStatic;

	@InjectMocks
	private TioMiddlewareCdpTagReceivableApiService underTest;

	private final EasyRandom easyRandom = new EasyRandom();
	private ProcessingIds processingIds;

	@BeforeEach
	void setUp() {
		underTest = new TioMiddlewareCdpTagReceivableApiService(bookkeepingPublisher, validator);
		tagReceivableMapperMockedStatic = mockStatic(TagReceivableMapper.class);
		tagSettlementMapperMockedStatic = mockStatic(TagSettlementMapper.class);
		processingIds = new ProcessingIds("companyDocument", "companyId", "correlationId");
	}

	@AfterEach
	void tearDown() {
		tagReceivableMapperMockedStatic.close();
		tagSettlementMapperMockedStatic.close();
	}

	@Test
	@DisplayName("Should handle valid receivables correctly")
	void sendReceivable_ValidReceivables_ShouldProcessCorrectly() {
		// Given
		TagReceivablesRequest request = easyRandom.nextObject(TagReceivablesRequest.class);
		TagReceivablesResponse expectedResponse = easyRandom.nextObject(TagReceivablesResponse.class);
		List<BKTransactionRequest> bkTransactionRequests = List.of(easyRandom.nextObject(BKTransactionRequest.class));

		// When
		when(TagReceivableMapper.mapToBKTransactionRequest(any(), any(), any(), any()))
				.thenReturn(bkTransactionRequests);
		when(TagReceivableMapper.mapResponse(any(), any(), any(), any())).thenReturn(expectedResponse);

		// Then
		TagReceivablesResponse response = assertDoesNotThrow(() -> underTest.sendReceivable(request, processingIds));
		assertNotNull(response);
		assertTrue(response.getReceivables().size() > 0);
	}

	@Test
	@DisplayName("Should throw exception for invalid receivables")
	void sendReceivable_InvalidReceivables_ShouldThrowException() {
		// Given
		TagReceivablesRequest request = TagReceivablesRequest.builder().receivables(Collections.emptyList()).build();

		// Then
		assertThrows(FailedBatchTransactionException.class, () -> underTest.sendReceivable(request, processingIds));
	}

	@Test
	@DisplayName("Valid settlements should be processed correctly")
	void sendSettlement_ValidSettlements_ShouldProcessCorrectly() {
		// Given
		TagSettlementsRequest request = createValidTagSettlementsRequest();
		TagSettlementsResponse expectedResponse = createValidTagSettlementsResponse();

		// When
		when(TagSettlementMapper.mapFrom(any(List.class), anyString(), anyString(), anyString()))
				.thenReturn(List.of(easyRandom.nextObject(BKTransactionRequest.class)));
		when(TagSettlementMapper.mapFrom(any(List.class), any(List.class), anyString(), anyString()))
				.thenReturn(expectedResponse);

		// Then
		TagSettlementsResponse response = assertDoesNotThrow(() -> underTest.sendSettlement(request, processingIds));
		assertNotNull(response);
		assertTrue(response.getSettlements().size() == 2);
		assertTrue(response.getErrors().isEmpty());
	}

	@Test
	@DisplayName("Should throw exception when all settlements are invalid")
	void sendSettlement_AllInvalidSettlements_ShouldThrowException() {
		// Given
		TagSettlementsRequest request = createInvalidTagSettlementsRequest();

		// When
		when(TagSettlementMapper.mapFrom(any(List.class), anyString(), anyString(), anyString()))
				.thenThrow(FailedBatchTransactionException.class);
		// Then
		assertThrows(FailedBatchTransactionException.class, () -> underTest.sendSettlement(request, processingIds));
	}

	@Test
	@DisplayName("Should handle publishing errors gracefully")
	void sendReceivable_PublishingError_ShouldHandleGracefully() {
		// Given
		TagReceivablesRequest request = easyRandom.nextObject(TagReceivablesRequest.class);
		List<BKTransactionRequest> bkTransactionRequests = List.of(easyRandom.nextObject(BKTransactionRequest.class));

		// Use the already created static mock
		when(TagReceivableMapper.mapToBKTransactionRequest(any(), any(), any(), any()))
				.thenReturn(bkTransactionRequests);

		// Simulate MessagePublishingException
		doThrow(new MessagePublishingException("Test Exception")).when(bookkeepingPublisher).publishAsync(anyString(),
				any(BKTransactionRequest.class), eq(BKTransactionDomainType.FINANCIAL_ASSET));

		// Then
		assertThrows(FailedBatchTransactionException.class, () -> underTest.sendReceivable(request, processingIds));
	}

	private TagSettlementsRequest createValidTagSettlementsRequest() {
		return TagSettlementsRequest.builder().idempotencyKey("303020ec-863a-4f24-9816-1c21964d9b3d")
				.settlements(List.of(
						createTagSettlement("TESTE-pub3", "CNPJ", "04684416119", "04684416119", "2024-07-12",
								"2024-07-15", "ACC", new BigInteger("300"), createTagBankAccount("CNPJ")),
						createTagSettlement("TESTE-pub5", "CPF", "04684416119", "04684416119", "2024-07-12",
								"2024-07-15", "ACC", new BigInteger("300"), createTagBankAccount("CPF"))))
				.build();
	}

	private TagSettlementsRequest createInvalidTagSettlementsRequest() {
		return TagSettlementsRequest.builder().idempotencyKey("303020ec-863a-4f24-9816-1c21964d9b3d")
				.settlements(List.of(
						createTagSettlement("TESTE-pub3", "CNPJ", null, "04684416119", "2024-07-12", "2024-07-15",
								"ACC", new BigInteger("300"), createTagBankAccount("CNPJ")),
						createTagSettlement("TESTE-pub5", "CPF", null, "04684416119", "2024-07-12", "2024-07-15", "ACC",
								new BigInteger("300"), createTagBankAccount("CPF"))))
				.build();
	}

	private TagSettlement createTagSettlement(String reference, String assetHolderDocumentType, String assetHolder,
			String originalAssetHolder, String settlementDate, String settlementObligationDate, String paymentScheme,
			BigInteger amount, TagBankAccount bankAccount) {
		return TagSettlement.builder().reference(reference).assetHolderDocumentType(assetHolderDocumentType)
				.assetHolder(assetHolder).originalAssetHolder(originalAssetHolder).settlementDate(settlementDate)
				.settlementObligationDate(settlementObligationDate).paymentScheme(paymentScheme).amount(amount)
				.bankAccount(bankAccount).build();
	}

	private TagBankAccount createTagBankAccount(String documentType) {
		return TagBankAccount.builder().branch("1234").account("567890").accountDigit("X").accountType("CC")
				.ispb("12345678").documentNumber("04684416119").documentType(documentType).build();
	}

	private TagSettlementsResponse createValidTagSettlementsResponse() {
		return TagSettlementsResponse.builder().processKey("correlationId").createdAt("2023-01-01T00:00:00Z")
				.settlements(
						List.of(new TagResponseItem("TESTE-pub3", "key-1"), new TagResponseItem("TESTE-pub5", "key-2")))
				.errors(Collections.emptyList()).build();
	}
}
