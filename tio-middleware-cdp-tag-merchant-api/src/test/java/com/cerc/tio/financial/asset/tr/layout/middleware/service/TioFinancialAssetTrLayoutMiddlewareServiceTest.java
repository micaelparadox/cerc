package com.cerc.tio.financial.asset.tr.layout.middleware.service;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagBankAccount;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagErrorResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.exception.FailedBatchTransactionException;
import com.cerc.tio.financial.asset.tr.layout.middleware.mapper.TagMerchantMapper;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.service.MerchantService;
import com.cerc.tio.libcdpcommon.util.domain.ProcessingIds;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TioFinancialAssetTrLayoutMiddlewareServiceTest {

    @Mock
    private MerchantService cadastraService;

    private Validator validator;

    private MockedStatic<TagMerchantMapper> tagMerchantMapper;

    private TioFinancialAssetTrLayoutMiddlewareService underTest;

    @BeforeEach
    void setUp() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
            validator = factory.getValidator();
            underTest = new TioFinancialAssetTrLayoutMiddlewareService(cadastraService, validator);
        }
        tagMerchantMapper = mockStatic(TagMerchantMapper.class);
    }

    @AfterEach
    void tearDown() {
        tagMerchantMapper.close();
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendMerchant_Should_return_response_When_called() {
        // given
        var tagRequest = buildValidRequest();
        tagMerchantMapper.when(() ->
                TagMerchantMapper.mapFrom(any(List.class), any(String.class), any(String.class), any(Boolean.class))
            ).thenReturn(List.of(new EasyRandom().nextObject(MerchantRequest.class)));
        tagMerchantMapper.when(() ->
                TagMerchantMapper.mapFrom(anyString(), any(TagRequest.class), any(List.class), any(String.class))
            ).thenReturn(new EasyRandom().nextObject(TagResponse.class));

        var merchantResponse = List.of(MerchantResponse.builder().companyOnwerId("1234").build());

        var apigeeHeader = new ProcessingIds("123456789", "1", "1");

        // when
        when(cadastraService.sendMerchant(any(List.class))).thenReturn(merchantResponse);
        TagResponse response = underTest.sendMerchant(tagRequest, apigeeHeader, false);

        // then
        assertNotNull(response);
    }

    @Test
    void sendMerchant_Should_throw_exception_When_called_with_invalid_request() {
        // given
        var tagRequest = TagRequest.builder().commercialEstablishments(List.of(
            new EasyRandom().nextObject(TagRequest.CommercialEstablishment.class)
        )).build();
        var apigeeHeader = new ProcessingIds("123456789", "1", "1");

        // then
        TagErrorResponse response =
            assertThrows(FailedBatchTransactionException.class, () ->
                underTest.sendMerchant(tagRequest, apigeeHeader, false)
            ).getTagErrorResponse();

        assertEquals(apigeeHeader.getCorrelationId(), response.getProcessKey());
        assertNotNull(response.getCreatedAt());
        assertThat(response.getErrors()).isNotEmpty();
    }

    @Test
    void sendMecharnt_Should_throw_exception_When_returned_processed_batch_all_fail(){
        // given
        var tagRequest = buildValidRequest();
        var apigeeHeader = new ProcessingIds("123456789", "1", "1");
        var merchantResponse = List.of(MerchantResponse.builder().errors(List.of(new MerchantResponse.Error("code", "message"))).build());

        // when
        when(cadastraService.sendMerchant(any(List.class))).thenReturn(merchantResponse);

        // then
        TagErrorResponse response =
            assertThrows(FailedBatchTransactionException.class, () ->
                underTest.sendMerchant(tagRequest, apigeeHeader, false)
            ).getTagErrorResponse();

        assertEquals(apigeeHeader.getCorrelationId(), response.getProcessKey());
        assertNotNull(response.getCreatedAt());
        assertThat(response.getErrors()).isNotEmpty();
    }

    private TagRequest buildValidRequest() {
        return TagRequest.builder()
            .commercialEstablishments(
                List.of(
                    TagRequest.CommercialEstablishment.builder()
                        .paymentSchemes(List.of("VCC"))
                        .documentType("CPF")
                        .documentNumber("16110626082")
                        .bankAccount(
                            TagBankAccount.builder()
                                .branch("1234")
                                .account("12346789")
                                .accountDigit("X1")
                                .accountType("CC")
                                .ispb("12345678")
                                .documentType("CPF")
                                .documentNumber("16110626082")
                                .build()
                        )
                        .build()
                )
            )
            .build();
    }
}