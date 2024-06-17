package com.cerc.tio.financial.asset.tr.layout.middleware.service;

import com.cerc.tio.financial.asset.tr.layout.middleware.controller.GlobalExceptionResponseHandler;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagErrorResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.exception.FailedBatchTransactionException;
import com.cerc.tio.financial.asset.tr.layout.middleware.mapper.TagMerchantMapper;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.service.MerchantService;
import com.cerc.tio.libcdpcommon.util.domain.ProcessingIds;
import com.cerc.tio.libcdpcommon.validation.ValidatableItem;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class TioFinancialAssetTrLayoutMiddlewareService {
    private final MerchantService cadastraService;
    private final Validator validator;

    private static final DateTimeFormatter ZONED_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    public TagResponse sendMerchant(
        final TagRequest request,
        final ProcessingIds processingIds,
        final boolean isUpdate
    ) {
        final List<TagRequest.CommercialEstablishment> validRequests = filterValidRequests(request);

        returnErrorResponseIfEntireBatchRequestIsInvalid(validRequests, request.getErrors(), processingIds.getCorrelationId());

        final List<MerchantRequest> merchantRequests =
            TagMerchantMapper.mapFrom(validRequests, processingIds.getCompanyDocument(), processingIds.getCompanyId(), isUpdate);

        List<MerchantResponse> merchantResponses = cadastraService.sendMerchant(merchantRequests);

        returnErrorResponseIfEntireBatchFailed(request, processingIds, merchantResponses);

        return TagMerchantMapper.mapFrom(processingIds.getCompanyDocument(), request, merchantResponses, processingIds.getCorrelationId());
    }

    /**
     * This method checks if the list of {@link TagRequest.CommercialEstablishment} are empty.
     * If they do, it throws a {@link FailedBatchTransactionException} with a {@link TagErrorResponse}.
     * to {@link GlobalExceptionResponseHandler} handle the exception and return the response
     *
     * @param filteredRequests The list of filtered {@link TagRequest.CommercialEstablishment}.
     * @param errors The list of errors from the original {@link TagRequest}.
     * @param correlationId The correlationId of the request.
     */
    private void returnErrorResponseIfEntireBatchRequestIsInvalid(
        List<TagRequest.CommercialEstablishment> filteredRequests,
        List<String> errors,
        String correlationId
    ) {
        if(filteredRequests.isEmpty())
            throw buildFailedBatchTransactionException(errors, correlationId);
    }

    /**
     * This method checks if all {@link MerchantResponse} contain errors.
     * If they do, it throws a {@link FailedBatchTransactionException} with a {@link TagErrorResponse}.
     * to {@link GlobalExceptionResponseHandler} handle the exception and return the response
     *
     * @param request The original TagRequest.
     * @param processingIds The ProcessingIds related to the request.
     * @param merchantResponses The list of MerchantResponses to check.
     */
    private void returnErrorResponseIfEntireBatchFailed(TagRequest request, ProcessingIds processingIds, List<MerchantResponse> merchantResponses) {
        if(
            merchantResponses.stream().noneMatch(merchantResponse ->
                merchantResponse.getErrors() == null || merchantResponse.getErrors().isEmpty()
            )
        ) {
            List<String> errors = merchantResponses.stream()
                .map(MerchantResponse::getErrors)
                .flatMap(List::stream)
                .map(MerchantResponse.Error::getMessage)
                .collect(Collectors.toList());
            errors.addAll(request.getErrors());

            throw buildFailedBatchTransactionException(errors, processingIds.getCorrelationId());
        }
    }

    /**
     * This method is responsible for filtering the valid requests from a {@link TagRequest}.
     * It first validates the {@link TagRequest},
     * then filters out the invalid {@link TagRequest.CommercialEstablishment}
     * and returns a list of valid ones.
     *
     * @param request The {@link TagRequest} to be filtered.
     * @return A list of valid {@link TagRequest.CommercialEstablishment}.
     */
    private List<TagRequest.CommercialEstablishment> filterValidRequests(final TagRequest request) {
        request.validate(validator);
        return ofNullable(request.getCommercialEstablishments())
            .map( ce-> ce.stream()
                .filter(ValidatableItem::isValid)
                .toList()
            ).orElseGet(Collections::emptyList);
    }

    private static FailedBatchTransactionException buildFailedBatchTransactionException(List<String> errors, String correlationId) {
        return new FailedBatchTransactionException(
            TagErrorResponse.builder()
                .errors(errors)
                .processKey(correlationId)
                .createdAt(ZonedDateTime.now().format(ZONED_DATE_TIME_FORMAT))
                .build()
        );
    }
}
