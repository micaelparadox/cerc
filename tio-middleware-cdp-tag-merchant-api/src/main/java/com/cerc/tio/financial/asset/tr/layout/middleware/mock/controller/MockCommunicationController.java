package com.cerc.tio.financial.asset.tr.layout.middleware.mock.controller;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.component.Stopwatch;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement.AdvancementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement.Body;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.communication.CommunicationExcessBody;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.communication.V2TerminationBody;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.contract.*;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.reconciliation.ConfirmResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement.RejectBody;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement.RejectResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement.SettlementsResponse;
import com.google.gson.JsonObject;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transaction-io/pa-receivables/v1/communication")
public class MockCommunicationController {

    @PostMapping(value = "/excess")
    @Stopwatch
    @WithSpan
    public ResponseEntity<CommunicationResponse> comunicacaoDeLiberacaoDeExcedenteGarantia(
            @RequestHeader Map<String, String> headers,
            @RequestBody CommunicationExcessBody request
    ) {
        var response = new CommunicationResponse();
        return ResponseEntity.accepted().body(response);
    }

    @PostMapping(value = "/v2/termination")
    @Stopwatch
    @WithSpan
    public ResponseEntity<CommunicationResponse> comunicacaoResilicaoPromessaDeCessao(
            @RequestHeader Map<String, String> headers,
            @RequestBody V2TerminationBody request
            ) {
        var response = new CommunicationResponse();
        return ResponseEntity.accepted().body(response);
    }
}
