package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementRejection;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsRejectionRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagResponseItem;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationConfig;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.FinancialAssetSettlement;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class TagSettlementRejectionMapper {

    private TagSettlementRejectionMapper() {
    }

    public static List<BKTransactionRequest> mapFrom(
            final List<TagSettlementRejection> settlementsRejection,
            final String companyId,
            final String correlationId
    ) {
        return ofNullable(settlementsRejection)
                .map(ce -> settlementsRejection.stream().map(settlementRejection ->
                                BKTransactionRequest.builder()
                                        .type("INACTIVATE")
                                        .receivedAt(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString())
                                        .notifications(List.of(BKNotificationConfig.builder()
                                                .eventName("PROCESSING_FINISHED")
                                                .eventUrl("http://subscription.cdp-tag")
                                                .build()))
                                        .requester(companyId)
                                        .requesterTransactionId(correlationId)
                                        .financialAssetSettlements(FinancialAssetSettlement.builder()
                                                .settlementId(settlementRejection.getKey())
                                                .build()
                                        ).build()
                        ).collect(Collectors.toList())
                ).orElse(Collections.emptyList());
    }

    public static TagSettlementsResponse mapFrom(
            final TagSettlementsRejectionRequest request,
            final String createdAt,
            final String correlationId
    ) {
        return TagSettlementsResponse.builder()
                .processKey(correlationId)
                .createdAt(createdAt)
                .settlements(ofNullable(request.getSettlements())
                        .map(ce -> ce.stream().map(receivable ->
                                        TagResponseItem.builder()
                                                .key(receivable.getKey())
                                                .build())
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList()))
                .build();
    }

}
