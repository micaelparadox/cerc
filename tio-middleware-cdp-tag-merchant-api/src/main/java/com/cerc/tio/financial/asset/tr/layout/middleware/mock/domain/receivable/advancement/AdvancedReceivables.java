package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement;

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
public class AdvancedReceivables {
    private String paymentScheme = "ACC";
    private String settlementObligationDate = "2020-02-13";
    private String advancedAmount = "300";
}
