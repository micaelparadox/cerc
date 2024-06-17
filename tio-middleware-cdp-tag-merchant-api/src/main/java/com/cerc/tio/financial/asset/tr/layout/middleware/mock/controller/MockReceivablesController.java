package com.cerc.tio.financial.asset.tr.layout.middleware.mock.controller;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.component.Stopwatch;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement.AdvancementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement.Body;
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
@RequestMapping(value = "/transaction-io/pa-receivables/v1/receivable")
public class MockReceivablesController {
    @PostMapping(value = "/advancement")
    @Stopwatch
    @WithSpan
    public ResponseEntity<com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement.Response> antecipacaoPosContratada(
            @RequestHeader Map<String, String> headers,
            @RequestBody Body request
    ) {

        var response = new com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement.Response();
        response.addAdvancementsItem(new AdvancementsResponse());

        return ResponseEntity.accepted().body(response);
    }

    //TODO: qual o retorno adequado para essa api?
    @PostMapping(value = "/reconciliation")
    @Stopwatch
    @WithSpan
    public ResponseEntity<JsonObject> conciliacaoDeAgenda(
            @RequestHeader Map<String, String> headers,
            @RequestBody Body request
    ) {

        var response = new JsonObject();

        return ResponseEntity.accepted().body(response);
    }

    //TODO: Essa api retorna 200, 204 e 400. Quando deve retornar 204 e qual conteudo?
    @PatchMapping(value = "/reconciliation/key/{reconciliationKey}")
    @Stopwatch
    @WithSpan
    public ResponseEntity<ConfirmResponse> confirmacaoDaConciliacaoDeAgenda(
            @RequestHeader Map<String, String> headers,
            @PathVariable  String reconciliationKey
            ) {

        var response = new ConfirmResponse();

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/settlement")
    @Stopwatch
    @WithSpan
    public ResponseEntity<com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement.Response> informeDeLiquidacao(
            @RequestHeader Map<String, String> headers,
            @RequestBody com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement.Body request
            ) {

        var response = new com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement.Response();
        response.addSettlementsItem(new SettlementsResponse());

        return ResponseEntity.accepted().body(response);
    }

    @PatchMapping(value = "/settlement/reject")
    @Stopwatch
    @WithSpan
    public ResponseEntity<RejectResponse> informeDeRejeicaoDeLiquidacao(
            @RequestHeader Map<String, String> headers,
            @RequestBody RejectBody request
            ) {

        var response = new RejectResponse();

        return ResponseEntity.accepted().body(response);
    }

    @PostMapping(value = "/contract")
    @Stopwatch
    @WithSpan
    public ResponseEntity<ContractsResponse> garantiaOuTrocaDeTitularidade(
            @RequestHeader Map<String, String> headers,
            @RequestBody ContractsPostBody request
    ) {

        var response = new ContractsResponse();

        return ResponseEntity.accepted().body(response);
    }

    @PatchMapping(value = "/contract")
    @Stopwatch
    @WithSpan
    public ResponseEntity<ContractsResponse> alteracaoDeGarantiaOuTrocaDeTitularidade(
            @RequestHeader Map<String, String> headers,
            @RequestBody ContractsPatchBody request
    ) {

        var response = new ContractsResponse();

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping(value = "/contract/pre-existing-contracts")
    @Stopwatch
    @WithSpan
    public ResponseEntity<PreExistingContractsResponse> consultaDeContratosPreExistentes(
            @RequestHeader Map<String, String> headers,
            @RequestBody PreExistingContractsBody request
    ) {
        var response = new PreExistingContractsResponse();

        return ResponseEntity.accepted().body(response);
    }

    @PostMapping(value = "/schedule/online")
    @Stopwatch
    @WithSpan
    public ResponseEntity<PreExistingContractsResponse> consultaOnlineRecebiveisCredor(
            @RequestHeader Map<String, String> headers,
            @RequestBody ScheduleOnlineBody request
    ) {
        return ResponseEntity.accepted().body(null);
    }
}
