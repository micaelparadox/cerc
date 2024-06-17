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
public class AdvancementsResponse {
    private String key = "303020ec-863a";
    private String reference = "P_54636";
}
