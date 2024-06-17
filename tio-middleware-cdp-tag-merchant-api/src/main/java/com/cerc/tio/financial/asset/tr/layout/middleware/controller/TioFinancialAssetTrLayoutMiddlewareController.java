package com.cerc.tio.financial.asset.tr.layout.middleware.controller;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.component.Stopwatch;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.service.TioFinancialAssetTrLayoutMiddlewareService;
import com.cerc.tio.libcdpcommon.util.HeaderUtil;
import com.cerc.tio.libcdpcommon.util.domain.ProcessingIds;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value= "/transaction-io/pa-receivables/v1/customer")
public class TioFinancialAssetTrLayoutMiddlewareController {
    private final TioFinancialAssetTrLayoutMiddlewareService service;

    @PostMapping(value = "/commercialEstablishment")
    @Stopwatch
    @WithSpan
    public ResponseEntity<TagResponse> registerMerchant(
        @RequestHeader Map<String, String> headers,
        @RequestBody TagRequest tagRequest
    ) {
        final ProcessingIds processingIds = HeaderUtil.serializeApigeeHeaders(headers);
        TagResponse tagResponse = service.sendMerchant(tagRequest, processingIds, false);
        return ResponseEntity.ok(tagResponse);
    }

    @PatchMapping(value = "/commercialEstablishment")
    @Stopwatch
    @WithSpan
    public ResponseEntity<TagResponse> updateMerchant(
        @RequestHeader Map<String, String> headers,
        @RequestBody TagRequest tagRequest
    ) {
        final ProcessingIds processingIds = HeaderUtil.serializeApigeeHeaders(headers);
        TagResponse tagResponse = service.sendMerchant(tagRequest, processingIds, true);
        return ResponseEntity.ok(tagResponse);
    }
}