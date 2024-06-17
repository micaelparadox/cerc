package com.cerc.tio.financial.asset.tr.layout.middleware.service;

import static com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionDomainType.*;
import static java.util.Optional.ofNullable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagSettlement;
import com.cerc.tio.financial.asset.tr.layout.middleware.mapper.TagAdvancementMapper;
import com.cerc.tio.financial.asset.tr.layout.middleware.mapper.TagReceivableMapper;
import com.cerc.tio.financial.asset.tr.layout.middleware.mapper.TagSettlementMapper;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionDomainType;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;
import com.cerc.tio.libcdpcommon.exception.FailedBatchTransactionException;
import com.cerc.tio.libcdpcommon.pubsub.publisher.BookkeepingPublisher;
import com.cerc.tio.libcdpcommon.pubsub.publisher.exception.MessagePublishingException;
import com.cerc.tio.libcdpcommon.util.domain.ProcessingIds;
import com.cerc.tio.libcdpcommon.validation.ValidatableItem;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TioMiddlewareCdpTagReceivableApiService {

	private final BookkeepingPublisher bookkeepingPublisher;

	private final Validator validator;

	public TagReceivablesResponse sendReceivable(final TagReceivablesRequest request,
			final ProcessingIds processingIds) {
		final List<TagReceivablesRequest.TagReceivable> validRequests = filterValidRequests(request);

		if (validRequests.isEmpty()) {
			log.warn("[{}] No valid receivables to process", processingIds.getCorrelationId());
			throw FailedBatchTransactionException.of(request.getErrors(), processingIds.getCorrelationId());
		}

		final List<BKTransactionRequest> receivableRequests = TagReceivableMapper.mapToBKTransactionRequest(
				validRequests, processingIds.getCompanyDocument(), processingIds.getCompanyId(),
				processingIds.getCorrelationId());

		try {
			publishRequestsToBookkepingAsynchronously(receivableRequests, FINANCIAL_ASSET);
		} catch (MessagePublishingException e) {
			List<String> errors = new ArrayList<>();
			errors.add(e.getMessage());
			throw FailedBatchTransactionException.of(errors, processingIds.getCorrelationId());
		}

		String createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();

		return TagReceivableMapper.mapResponse(validRequests, createdAt, processingIds.getCorrelationId(),
				processingIds.getCompanyDocument());
	}

	public TagAdvancementsResponse sendAdvancement(final TagAdvancementsRequest request, final ProcessingIds processingIds) {
		List<String> errors = new ArrayList<>();

		List<TagAdvancementsRequest.TagAdvancement> validAdvancements = filterValidAdvancements(request, errors);

		final List<BKTransactionRequest> advancementRequests = TagAdvancementMapper.mapFrom(validAdvancements, errors,
				processingIds.getCompanyDocument(), processingIds.getCompanyId(), processingIds.getCorrelationId());

		try {
			publishRequestsToBookkepingAsynchronously(advancementRequests, ANTICIPATION);
		} catch (MessagePublishingException e) {
			errors.add(e.getMessage());
			throw FailedBatchTransactionException.of(errors, processingIds.getCorrelationId());
		}

		return TagAdvancementMapper.mapFrom(advancementRequests, errors, processingIds.getCorrelationId(),
				processingIds.getCompanyDocument());
	}

	public TagSettlementsResponse sendSettlement(final TagSettlementsRequest tagRequest,final ProcessingIds processingIds) {
		List<String> errors = new ArrayList<>();

		List<TagSettlement> validSettlements = filterValidSettlements(tagRequest, errors);

		final List<BKTransactionRequest> settlementRequests = TagSettlementMapper.mapFrom(validSettlements,processingIds.getCompanyDocument(), processingIds.getCompanyId(), processingIds.getCorrelationId());

		try {
			publishRequestsToBookkepingAsynchronously(settlementRequests, FINANCIAL_ASSET_SETTLEMENTS);
		} catch (MessagePublishingException e) {
			errors.add(e.getMessage());
			throw FailedBatchTransactionException.of(errors, processingIds.getCorrelationId());
		}

		return TagSettlementMapper.mapFrom(settlementRequests, errors, processingIds.getCorrelationId(),
				processingIds.getCompanyDocument());
	}

	private List<TagSettlement> filterValidSettlements(TagSettlementsRequest request, List<String> errors) {
		request.validate(validator);
		errors.addAll(request.getErrors());
		return ofNullable(request.getSettlements()).map(req -> req.stream().filter(ValidatableItem::isValid).toList())
				.orElseGet(Collections::emptyList);
	}

	private void publishRequestsToBookkepingAsynchronously(List<BKTransactionRequest> transactionRequests,
			BKTransactionDomainType domainType) throws MessagePublishingException {
		for (BKTransactionRequest transactionRequest : transactionRequests) {
			bookkeepingPublisher.publishAsync(transactionRequest.getRequesterTransactionId(), transactionRequest,
					domainType);
		}
	}

	private List<TagReceivablesRequest.TagReceivable> filterValidRequests(final TagReceivablesRequest request) {
		request.validate(validator);

		return ofNullable(request.getReceivables()).map(req -> req.stream().filter(ValidatableItem::isValid).toList())
				.orElseGet(Collections::emptyList);
	}

	private List<TagAdvancementsRequest.TagAdvancement> filterValidAdvancements(TagAdvancementsRequest request,
			List<String> errors) {
		request.validate(validator);
		errors.addAll(request.getErrors());

		return ofNullable(request.getAdvancements()).map(req -> req.stream().filter(ValidatableItem::isValid).toList())
				.orElseGet(Collections::emptyList);
	}
}
