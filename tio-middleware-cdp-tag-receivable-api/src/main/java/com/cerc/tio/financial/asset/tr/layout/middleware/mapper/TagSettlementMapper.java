package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import static java.util.Optional.ofNullable;
import static com.cerc.tio.libcdpcommon.util.FormatterUtil.formatValue;


import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagResponseItem;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagSettlement;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.AccountInfo;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationConfig;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.FinancialAssetSettlement;

public class TagSettlementMapper {

	private static final DateTimeFormatter ZONED_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

	private static final String TYPE = "CREATE";
	private static final String EVENT_NAME = "PROCESSING_FINISHED";
	private static final String EVENT_URL = "http://subscription.cdp-tag";

	private TagSettlementMapper() {
	}

	public static List<BKTransactionRequest> mapFrom(final List<TagSettlement> settlements,
			final String companyDocument, final String companyId, final String correlationId) {

		return ofNullable(settlements).map(ce -> settlements.stream().map(settlement ->

				BKTransactionRequest.builder()
					.type(TYPE)
					.receivedAt(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString())
					.notifications(
							List.of(BKNotificationConfig.builder()
									.eventName(EVENT_NAME)
									.eventUrl(EVENT_URL).build())
					)
					.requester(companyId).requesterTransactionId(correlationId)
					.financialAssetSettlements(FinancialAssetSettlement.builder()
							.settlementId(settlement.getFinancialAssetSettlementKey(companyDocument))
							.assetCommitmentHash(settlement.getFinancialAssetCommitmentHash(companyDocument))
							.reference(settlement.getReference()).date(settlement.getSettlementDate())
							.value(formatValue(settlement.getAmount()))
							.assetOwner(settlement.getAssetHolder())
							.accountInfo(AccountInfo.builder().agency(settlement.getBankAccount().getBranch())
									.number(String.format("%s-%s", settlement.getBankAccount().getAccount(),
											settlement.getBankAccount().getAccountDigit().replace("X", "0")))
									.type(AccountInfo.AccountType.getTypeByCode(settlement.getBankAccount().getAccountType()) )
									.ispb(settlement.getBankAccount().getIspb())
									.holderDocument(settlement.getBankAccount().getDocumentNumber()).build())
							.build())
					.build())
				.collect(Collectors.toList()))
				.orElse(Collections.emptyList());
	}

	public static TagSettlementsResponse mapFrom(
			final  List<BKTransactionRequest> validSettlementList,
			final List<String> errors,
			final String correlationId,
			final String companyDocument
	) {
		String createdAt = ZonedDateTime.now().format(ZONED_DATE_TIME_FORMAT);
		return TagSettlementsResponse.builder()
				.processKey(correlationId)
				.createdAt(createdAt)
				.errors(errors)
				.settlements(ofNullable(validSettlementList)
						.map(ce -> ce.stream().map(settlement ->
								{
									return TagResponseItem.builder()
											.reference(settlement.getFinancialAssetSettlements().getReference())
											.key(settlement.getFinancialAssetSettlements().getSettlementId())
											.build();
								})
								.collect(Collectors.toList()))
						.orElse(Collections.emptyList()))
				.build();
	}

}
