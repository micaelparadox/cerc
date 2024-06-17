package com.cerc.tio.financial.asset.tr.layout.middleware.controller;

import static java.util.Objects.isNull;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.component.Stopwatch;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.service.TioMiddlewareCdpTagReceivableApiService;
import com.cerc.tio.libcdpcommon.util.HeaderUtil;
import com.cerc.tio.libcdpcommon.util.domain.ProcessingIds;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TioMiddlewareCdpTagReceivableApiController {
	private final TioMiddlewareCdpTagReceivableApiService service;

	@PostMapping(value = "/receivable")
	@Stopwatch
	@WithSpan
	public ResponseEntity<TagReceivablesResponse> registerReceivable(@RequestHeader Map<String, String> headers,
			@RequestBody TagReceivablesRequest tagRequest) {

		final ProcessingIds processingIds = HeaderUtil.serializeApigeeHeaders(headers);

		TagReceivablesResponse response = service.sendReceivable(tagRequest, processingIds);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping(value = "/receivable/advancement")
	@Stopwatch
	@WithSpan
	public ResponseEntity<TagAdvancementsResponse> registerAdvancement(@RequestHeader Map<String, String> headers,
			@Valid @RequestBody TagAdvancementsRequest tagRequest) {

		final ProcessingIds processingIds = HeaderUtil.serializeApigeeHeaders(headers);

		TagAdvancementsResponse response = service.sendAdvancement(tagRequest, processingIds);

		if (isNull(response.getAdvancements()) || response.getAdvancements().isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PatchMapping(value = "/receivable/settlement")
	@Stopwatch
	@WithSpan
	public ResponseEntity<?> registerSettlement(@RequestHeader Map<String, String> headers,
			@Valid @RequestBody TagSettlementsRequest tagRequest) {
		final ProcessingIds processingIds = HeaderUtil.serializeApigeeHeaders(headers);

		TagSettlementsResponse response = service.sendSettlement(tagRequest, processingIds);

		if (response.getSettlements().isEmpty() && !response.getErrors().isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		else if (!response.getSettlements().isEmpty() && !response.getErrors().isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
