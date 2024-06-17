package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagBankAccount;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagSettlement;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.AccountInfo;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationConfig;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.SimplifiedFinancialAsset;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.cerc.tio.libcdpcommon.util.FormatterUtil.formatValue;
import static java.util.Optional.ofNullable;

public class TagReceivableMapper {


    private static final String TYPE = "CREATE";
    private static final String EVENT_NAME = "PROCESSING_FINISHED";
    private static final String EVENT_URL = "http://subscription.cdp-tag";



    public static List<BKTransactionRequest> mapToBKTransactionRequest(
        final List<TagReceivablesRequest.TagReceivable> receivables,
        final String companyDocument,
        final String companyId,
        final String correlationId
    ) {
        return ofNullable(receivables)
            .map(r -> r.stream()
                .map(receivable -> BKTransactionRequest.builder()
                    .type(TYPE)
                    .receivedAt(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString())
                    .notifications(List.of(BKNotificationConfig.builder()
                        .eventName(EVENT_NAME)
                        .eventUrl(EVENT_URL)
                        .build()))
                    .requester(companyId)
                    .requesterTransactionId(correlationId)
                    .financialAsset(SimplifiedFinancialAsset.builder()
                            .financialAssetId(receivable.getReceivableKey(companyDocument))
                            .payer(companyDocument)
                            .commitmentHash(String.format("%s_%s_%s_%s_%s",receivable.getOriginalAssetHolder(), receivable.getOriginalAssetHolder(), companyDocument, receivable.getPaymentScheme(), receivable.getDueDate()))
                            .originalOwner(receivable.getOriginalAssetHolder())
                            .owner(receivable.getOriginalAssetHolder())
                            .schemeCode(receivable.getPaymentScheme())
                            .expectedSettlementDate(receivable.getDueDate())
                            .externalReference(receivable.getReference())
                            .expectedSettlementValue(formatValue(receivable.getAmount()))
                            .actualValue(formatValue(receivable.getAmount()))
                            .preNegotiatedValue(formatValue(receivable.getPrePaidAmount()))
                            .parties(handleBankAccount(receivable.getBankAccount()))
                            .settlements(handleSettlements(receivable.getSettlements()))
                            .build()
                    ).build()
                ).toList()
            ).orElse(Collections.emptyList());
    }

    public static TagReceivablesResponse mapResponse(
        final List<TagReceivablesRequest.TagReceivable> validRequests,
        final String createdAt,
        final String correlationId,
        final String companyDocument
    ) {
        return TagReceivablesResponse.builder()
            .processKey(correlationId)
            .createdAt(createdAt)
            .receivables(ofNullable(validRequests)
                .map(r -> r.stream().map(receivable ->
                        TagReceivablesResponse.TagReceivableResponse.builder()
                            .reference(receivable.getReference())
                            .dueDate(receivable.getDueDate())
                            .originalAssetHolderDocumentType(receivable.getOriginalAssetHolderDocumentType())
                            .originalAssetHolder(receivable.getOriginalAssetHolder())
                            .paymentScheme(receivable.getPaymentScheme())
                            .key(receivable.getReceivableKey(companyDocument))
                            .build())
                    .toList())
                .orElse(Collections.emptyList()))
            .build();
    }

    private static List<SimplifiedFinancialAsset.Party> handleBankAccount(TagBankAccount bankAccount) {
        return Optional.ofNullable(bankAccount)
            .map(ba -> List.of(
                    SimplifiedFinancialAsset.Party.builder()
                        .documentHash(ba.getDocumentNumber())
                        .paymentInfos(Collections.singletonList(buildAccountInfo(ba)))
                        .build()
                )
            ).orElse(Collections.emptyList());
    }

    private static AccountInfo buildAccountInfo(TagBankAccount bankAccount) {
        return AccountInfo.builder()
            .agency(bankAccount.getBranch())
            .number(String.format(
                "%s-%s",
                bankAccount.getAccount(),
                bankAccount.getAccountDigit().replace("X", "0")
            ))
            .type(AccountInfo.AccountType.getTypeByCode(bankAccount.getAccountType()))
            .ispb(bankAccount.getIspb())
            .holderDocument(bankAccount.getDocumentNumber())
            .build();
    }

    private static List<SimplifiedFinancialAsset.Settlement> handleSettlements(List<TagSettlement> settlements) {
        return ofNullable(settlements)
            .map(liq -> settlements.stream().map(settlement ->
                    SimplifiedFinancialAsset.Settlement.builder()
                        .assetOwner(settlement.getAssetHolder())
                        .value(formatValue(settlement.getAmount()))
                        .date(settlement.getSettlementDate())
                        .reference(settlement.getReference())
                        .accountInfo(ofNullable(settlement.getBankAccount())
                            .map(TagReceivableMapper::buildAccountInfo)
                            .orElse(null))
                        .build())
                .toList()
            ).orElse(Collections.emptyList());
    }
}
