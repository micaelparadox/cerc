package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RejectSettlements {
    private String key = "e07d6c53-7dc0-429d-a1da-d76f955f6dfd";
    private String reasonDetails = "Liquidação não executada.";
}
