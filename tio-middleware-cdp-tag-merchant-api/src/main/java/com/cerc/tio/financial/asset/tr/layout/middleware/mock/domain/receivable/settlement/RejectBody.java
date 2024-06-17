package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RejectBody {
    private String idempotencyKey = "303020ec-863a-4f24-9816-1c21964d9b3d";
    private List<RejectSettlements> settlements = null;

}

