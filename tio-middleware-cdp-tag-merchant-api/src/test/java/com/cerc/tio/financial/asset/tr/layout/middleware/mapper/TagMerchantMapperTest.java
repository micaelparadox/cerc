package com.cerc.tio.financial.asset.tr.layout.middleware.mapper;

import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagBankAccount;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

class TagMerchantMapperTest {

    @Test
    void mapFrom_Should_return_MerchantRequest_When_given_TagRequest() {
        // Given
        final List<TagRequest.CommercialEstablishment> tagRequest = buildGivenTagRequest(true).getCommercialEstablishments();
        final String companyDocument = "12345678901";
        final String companyId = "12345678901";

        // When
        final List<MerchantRequest> result = TagMerchantMapper.mapFrom(tagRequest, companyDocument, companyId, true);

        // Then
        final List<MerchantRequest> expected = buildExpectedMerchantRequest(companyDocument, companyId);
        for (int i = 0; i < result.size(); i++) {
            final MerchantRequest actual = result.get(i);
            final MerchantRequest expectedMerchantRequest = expected.get(i);
            assertThat(actual).usingRecursiveComparison().isEqualTo(expectedMerchantRequest);
        }
    }

    @Test
    void mapFrom_Should_return_TagResponse_When_given_TagRequest() {
        // Given
        final String companyDocument = "12345678901";
        final TagRequest tagRequest = buildGivenTagRequest(true);
        final List<MerchantResponse> merchantResponses = buildGivenMerchantResponses(companyDocument);
        final String correlationId = "12345678901";

        // When
        final TagResponse result = TagMerchantMapper.mapFrom(companyDocument, tagRequest, merchantResponses, correlationId);

        // Then
        final TagResponse expected = buildExpectedTagResponse(correlationId);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void mapFrom_Should_not_throw_exception_When_bankAccount_is_null() {
        // Given
        final String companyDocument = "12345678901";
        final var tagRequest = buildGivenTagRequest(false).getCommercialEstablishments();
        final String companyId = "12345678901";

        // Then
        List<MerchantRequest> merchantRequests = assertDoesNotThrow(() -> TagMerchantMapper.mapFrom(tagRequest, companyDocument, companyId, true));
        assertNull(merchantRequests.getFirst().getBankAccount());
    }

    private TagRequest buildGivenTagRequest(boolean buildBankAccount) {
        return TagRequest.builder()
            .commercialEstablishments(List.of(
                TagRequest.CommercialEstablishment.builder()
                    .documentType("CPF")
                    .documentNumber("12345678901")
                    .enabled(true)
                    .paymentSchemes(List.of("scheme1", "scheme2"))
                    .bankAccount(buildBankAccount
                        ? TagBankAccount.builder()
                            .branch("1234")
                            .account("123456")
                            .accountDigit("X")
                            .accountType("CC")
                            .ispb("12345678")
                            .documentNumber("12345678901")
                            .build()
                        : null
                    ).build()
            ))
            .build();
    }

    private List<MerchantRequest> buildExpectedMerchantRequest(final String companyDocument, final String companyId) {
        return List.of(
            MerchantRequest.builder()
                .typeOfDocumentFinalReceivingUser(MerchantRequest.TypeOfDocumentFinalReceivingUser.CPF)
                .statusId(MerchantRequest.StatusId.ACTIVE)
                .finalReceivingUser("12345678901")
                .schemeList(List.of("scheme1", "scheme2"))
                .bankAccount(MerchantRequest.BankAccount.builder()
                    .agency("1234")
                    .number("123456-0")
                    .type("CC")
                    .ispb("12345678")
                    .holderDocument("12345678901")
                    .build())
                .typeOfOperation(MerchantRequest.TypeOfOperation.UPDATE)
                .externalReference(String.format("TAG_%s_%s", companyDocument, "12345678901"))
                .companyOwnerId(companyId)
                .typeOfFinalReceivingUser("1")
                .build()
        );
    }

    private List<MerchantResponse> buildGivenMerchantResponses(final String companyDocument) {
        return List.of(
            MerchantResponse.builder()
                .protocol("12345678901")
                .finalReceivingUser("12345678902")
                .status(0)
                .createdAt("2021-08-10T00:00:00Z")
                .updatedAt("2021-08-10T00:00:00Z")
                .externalReference(String.format("TAG_%s_%s", companyDocument, "12345678901"))
                .build()
        );
    }

    private TagResponse buildExpectedTagResponse(final String correlationId) {
        return TagResponse.builder()
            .commercialEstablishments(
                List.of(
                    TagResponse.CommercialEstablishment.builder()
                        .key("12345678901")
                        .documentType("CPF")
                        .documentNumber("12345678902")
                        .enabled(true)
                        .paymentSchemes(List.of("scheme1", "scheme2"))
                        .bankAccount(TagBankAccount.builder()
                            .branch("1234")
                            .account("123456")
                            .accountDigit("X")
                            .accountType("CC")
                            .ispb("12345678")
                            .documentNumber("12345678901")
                            .build())
                        .processKey(correlationId)
                        .createdAt("2021-08-10T00:00:00Z")
                        .updatedAt("2021-08-10T00:00:00Z")
                        .build()
                )
            ).build();
    }

}