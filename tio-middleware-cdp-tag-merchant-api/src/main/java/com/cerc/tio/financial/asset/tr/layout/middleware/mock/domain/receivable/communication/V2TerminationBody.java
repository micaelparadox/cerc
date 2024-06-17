package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.communication;

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
public class V2TerminationBody {
    private String communicationType = "1";
    private String communicator = "99999999999999";
    private String assetHolder = "99999999999999";
    private String acquirer = "99999999999999";
    private String contractKey = "6E73EC4D-D420-4D2B-82B5-A2812F6399D4";
    private String optinKey;
}
