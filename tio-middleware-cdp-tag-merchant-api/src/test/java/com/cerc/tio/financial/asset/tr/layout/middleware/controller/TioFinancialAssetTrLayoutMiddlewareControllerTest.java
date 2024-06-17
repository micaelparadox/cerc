package com.cerc.tio.financial.asset.tr.layout.middleware.controller;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.service.TioFinancialAssetTrLayoutMiddlewareService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TioFinancialAssetTrLayoutMiddlewareControllerTest {
    @Mock
    private TioFinancialAssetTrLayoutMiddlewareService service;

    @InjectMocks
    private TioFinancialAssetTrLayoutMiddlewareController underTest;

    @Test
    void registerMerchant_Should_return_response_When_called() {
        // given
        var tagRequest = new EasyRandom().nextObject(TagRequest.class);
        Map<String, String> headers = Map.of(
            "company-document", "companyDocument",
            "company-id", "companyId"
        );
        // when
        when(service.sendMerchant(any(), any(), anyBoolean()))
            .thenReturn(TagResponse.builder().build());
        var response = underTest.registerMerchant(headers, tagRequest);
        // then
        assertNotNull(response);
    }

    @Test
    void updateMerchant_Should_return_response_When_called() {
        // given
        var tagRequest = new EasyRandom().nextObject(TagRequest.class);
        Map<String, String> headers = Map.of(
            "company-document", "companyDocument",
            "company-id", "companyId"
        );
        // when
        when(service.sendMerchant(any(), any(), anyBoolean()))
            .thenReturn(TagResponse.builder().build());
        var response = underTest.updateMerchant(headers, tagRequest);
        // then
        assertNotNull(response);
    }
}