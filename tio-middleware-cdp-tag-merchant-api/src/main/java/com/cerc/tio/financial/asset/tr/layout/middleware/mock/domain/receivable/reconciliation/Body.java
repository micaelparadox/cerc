package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.reconciliation;

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
public class Body {
    private String reconciliationDate = "2020-07-02";
    private Boolean compact = false;

}

