package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantResponse;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class TagMerchantMapper {

    private TagMerchantMapper() {}

    public static List<MerchantRequest> mapFrom(
        final List<TagRequest.CommercialEstablishment> commercialEstablishments,
        final String companyDocument,
        final String companyId,
        final boolean isUpdate
    ) {
        return ofNullable(commercialEstablishments)
            .map(ce -> commercialEstablishments.stream().map(commercialEstablishment ->
                MerchantRequest.builder()
                    .typeOfDocumentFinalReceivingUser(MerchantRequest.TypeOfDocumentFinalReceivingUser.valueOf(commercialEstablishment.getDocumentType()))
                    .finalReceivingUser(commercialEstablishment.getDocumentNumber())
                    .statusId(getStatusId(isUpdate, commercialEstablishment.getEnabled()))
                    .schemeList(commercialEstablishment.getPaymentSchemes())
                    .bankAccount(
                        ofNullable(commercialEstablishment.getBankAccount()).map(bankAccount ->
                            MerchantRequest.BankAccount.builder()
                                .agency(bankAccount.getBranch())
                                .number(String.format(
                                    "%s-%s",
                                    bankAccount.getAccount(),
                                    bankAccount.getAccountDigit().replace("X", "0")
                                ))
                                .type(bankAccount.getAccountType())
                                .ispb(bankAccount.getIspb())
                                .holderDocument(bankAccount.getDocumentNumber())
                                .build()
                        ).orElse(null)
                    ).typeOfOperation(isUpdate ? MerchantRequest.TypeOfOperation.UPDATE : MerchantRequest.TypeOfOperation.REGISTER)
                    .externalReference(createExternalReference(companyDocument, commercialEstablishment.getDocumentNumber()))
                    .companyOwnerId(companyId)
                    .typeOfFinalReceivingUser("1")
                    .build())
                .toList())
            .orElse(Collections.emptyList());
    }

    /**
     * Get status id based on enabled request variable, only if request is update and enabled is present
     * @param isUpdate boolean representing if request is from update route
     * @param enabled boolean from request representing if merchant is enabled
     * @return status id
     */
    private static MerchantRequest.StatusId getStatusId(final boolean isUpdate, final Boolean enabled) {
        if(isUpdate && Boolean.TRUE.equals(enabled))
            return MerchantRequest.StatusId.ACTIVE;
        if(isUpdate && Boolean.FALSE.equals(enabled))
            return MerchantRequest.StatusId.INACTIVE;
        return null;
    }

    public static TagResponse mapFrom(
        final String companyDocument,
        final TagRequest request,
        final List<MerchantResponse> merchantResponses,
        final String correlationId
    ) {

        var requestsAndResponses = groupRequestAndResponseByExternalReferences(companyDocument, request, merchantResponses);

        return TagResponse.builder()
            .commercialEstablishments(
                requestsAndResponses.values().stream().map(requestAndResponse -> {
                    TagRequest.CommercialEstablishment commercialEstablishment = requestAndResponse.getLeft();
                    MerchantResponse merchantResponse = requestAndResponse.getRight();
                    return TagResponse.CommercialEstablishment.builder()
                        .key(merchantResponse.getProtocol())
                        .documentType(commercialEstablishment.getDocumentType())
                        .documentNumber(merchantResponse.getFinalReceivingUser())
                        .enabled(merchantResponse.getStatus() == 0)
                        .paymentSchemes(commercialEstablishment.getPaymentSchemes())
                        .bankAccount(commercialEstablishment.getBankAccount())
                        .processKey(correlationId)
                        .createdAt(merchantResponse.getCreatedAt())
                        .updatedAt(merchantResponse.getUpdatedAt())
                        .build();
                }).toList())
            .build();
    }

    private static Map<String, Pair<TagRequest.CommercialEstablishment, MerchantResponse>> groupRequestAndResponseByExternalReferences(String companyDocument, TagRequest request, List<MerchantResponse> merchantResponses) {
        return ofNullable(request.getCommercialEstablishments())
            .map(ces ->
                filterRequestByResponsePresence(companyDocument, merchantResponses, ces)
                    .collect(Collectors.toMap(
                        commercialEstablishment -> createExternalReference(companyDocument, commercialEstablishment.getDocumentNumber()),
                        commercialEstablishment -> Pair.of(commercialEstablishment, findMerchantResponseByExternalReference(merchantResponses, companyDocument, commercialEstablishment.getDocumentNumber()))
                    ))
            )
            .orElse(Collections.emptyMap());
    }

    private static Stream<TagRequest.CommercialEstablishment> filterRequestByResponsePresence(String companyDocument, List<MerchantResponse> merchantResponses, List<TagRequest.CommercialEstablishment> ce) {
        return ce.stream()
            .filter(commercialEstablishment ->
                matchMerchantResponseByExternalReference(merchantResponses, companyDocument, commercialEstablishment.getDocumentNumber()));
    }

    private static boolean matchMerchantResponseByExternalReference(
        final List<MerchantResponse> merchantResponses,
        final String companyDocument,
        final String documentNumber
    ) {
        return merchantResponses.stream().anyMatch(mr -> mr.getExternalReference().equals(createExternalReference(companyDocument, documentNumber)));
    }

    private static MerchantResponse findMerchantResponseByExternalReference(
        final List<MerchantResponse> merchantResponses,
        final String companyDocument,
        final String documentNumber
    ) {
        return merchantResponses.stream()
            .filter(mr -> mr.getExternalReference().equals(createExternalReference(companyDocument, documentNumber)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Merchant response not found"));
    }

    private static String createExternalReference(String companyDocument, String documentNumber) {
        return String.format("TAG_%s_%s", companyDocument, documentNumber);
    }
}
