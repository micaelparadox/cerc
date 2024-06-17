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
public class RejectResponse {
    private String processKey = null;
    private String createdAt = null;

}
