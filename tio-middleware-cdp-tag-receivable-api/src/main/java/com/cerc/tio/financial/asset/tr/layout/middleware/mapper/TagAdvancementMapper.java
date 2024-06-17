package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import com.cerc.tio.libcdpcommon.domain.bookkeeping.*;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagResponseItem;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cerc.tio.libcdpcommon.util.FormatterUtil.formatValue;
import static java.util.Optional.ofNullable;



@Slf4j
public class TagAdvancementMapper {


    private static final String TYPE = "CREATE";
    private static final String EVENT_NAME = "PROCESSING_FINISHED";
    private static final String EVENT_URL = "http://subscription.cdp-tag";
    private static final String EXTERNAL_MANAGEMENT = "EXTERNAL_MANAGEMENT";
    private static final DateTimeFormatter ZONED_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    private TagAdvancementMapper() {
    }

    public static List<BKTransactionRequest> mapFrom(
            final List<TagAdvancementsRequest.TagAdvancement> advancements,
            List<String> errors,
            final String companyDocument,
            final String companyId,
            final String correlationId
    ) {
        return ofNullable(advancements)
                .map(ce -> advancements.stream().map(advancement ->
                                {
                                    try {
                                        return BKTransactionRequest.builder()
                                                .type(TYPE)
                                                .receivedAt(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString())
                                                .notifications(List.of(BKNotificationConfig.builder()
                                                        .eventName(EVENT_NAME)
                                                        .eventUrl(EVENT_URL)
                                                        .build()))
                                                .requester(companyId)
                                                .requesterTransactionId(correlationId)
                                                .contract(Contract.builder()
                                                        .contractId(advancement.getAdvancementContractId(companyDocument))
                                                        .type(Contract.Type.ANTECIPATION)
                                                        .trManagesCollateral(EXTERNAL_MANAGEMENT)
                                                        .reference(advancement.getReference())
                                                        .participantContractIdentifier(advancement.getReference())
                                                        .effectType(Contract.EffectType.OWNERSHIP_CHANGE)
                                                        .operationType(Contract.OperationType.CESSION)
                                                        .signatureDate(Instant.now().truncatedTo(ChronoUnit.DAYS).toString())
                                                        .expirationDate(advancement.getOperationExpectedSettlementDate())
                                                        .contractorHash(advancement.getAssetHolder())
                                                        .originalRequesterHash(companyDocument)
                                                        .ownerHash(companyDocument)
                                                        .value(formatValue(advancement.getOperationValue()))
                                                        .minCollateralValue(formatValue(advancement.getOperationValue()))
                                                        .effectFilters(List.of(
                                                                        Contract.EffectFilter.builder()
                                                                                .externalReference(advancement.getReference())
                                                                                .assetOwnerDocumentHash(advancement.getAssetHolder())
                                                                                .valueType(Contract.EffectFilter.ValueType.FIXED)
                                                                                .value(formatValue(advancement.getOperationValue()))
                                                                                .accountInfo(null)
                                                                                .assetSettlementStartDate(
                                                                                        advancement.getAdvancedReceivables().stream().map(
                                                                                                TagAdvancementsRequest.TagAdvancement.TagAdvancedReceivables::getSettlementObligationDate).min(Comparator.naturalOrder()).orElse("")
                                                                                )
                                                                                .assetSettlementEndDate(
                                                                                        advancement.getAdvancedReceivables().stream().map(
                                                                                                TagAdvancementsRequest.TagAdvancement.TagAdvancedReceivables::getSettlementObligationDate).max(Comparator.naturalOrder()).orElse("")
                                                                                )
                                                                                .schemes(advancement.getAdvancedReceivables().stream().map(
                                                                                        TagAdvancementsRequest.TagAdvancement.TagAdvancedReceivables::getPaymentScheme).distinct().collect(Collectors.toList())
                                                                                )
                                                                                .payees(List.of(advancement.getAssetHolder()))
                                                                                .payers(List.of(companyDocument))
                                                                                .assets(
                                                                                        advancement.getAdvancedReceivables().stream().map(tagAdvancedReceivable ->
                                                                                                Contract.ContractEffectAsset.builder()
                                                                                                        .assetCommitmentHash(String.format(
                                                                                                                "%s_%s_%s_%s_%s",
                                                                                                                advancement.getAssetHolder(),
                                                                                                                advancement.getAssetHolder(),
                                                                                                                companyDocument,
                                                                                                                tagAdvancedReceivable.getPaymentScheme(),
                                                                                                                tagAdvancedReceivable.getSettlementObligationDate()
                                                                                                        ))
                                                                                                        .restrictedValue(formatValue(tagAdvancedReceivable.getAdvancedAmount()))
                                                                                                        .active(Boolean.TRUE)
                                                                                                        .build()
                                                                                        ).collect(Collectors.toList())
                                                                                )
                                                                                .build()
                                                                )
                                                        ).build()
                                                ).build();
                                    } catch (NoSuchAlgorithmException e) {
                                        log.error(e.toString());
                                        log.error("Error generating contract id", advancement.getReference() + " " + advancement.getAssetHolder());
                                        errors.add("Error generating contract id for reference " + advancement.getReference());
                                        return null;
                                    }
                                }
                        ).filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public static TagAdvancementsResponse mapFrom(
            final  List<BKTransactionRequest> validAdvancementsList,
            final List<String> errors,
            final String correlationId,
            final String companyDocument
    ) {
        String createdAt = ZonedDateTime.now().format(ZONED_DATE_TIME_FORMAT);
        return TagAdvancementsResponse.builder()
                .processKey(correlationId)
                .createdAt(createdAt)
                .errors(errors)
                .advancements(ofNullable(validAdvancementsList)
                        .map(ce -> ce.stream().map(receivable ->
                                {
                                    return TagResponseItem.builder()
                                            .reference(receivable.getContract().getReference())
                                            .key(receivable.getContract().getContractId())
                                            .build();
                                })
                                .collect(Collectors.toList()))
                        .orElse(Collections.emptyList()))
                .build();
    }


}
