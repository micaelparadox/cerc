package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.settlement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private List<SettlementsResponse> settlements = null;
    private String processKey = null;
    private String createdAt = null;

    public Response addSettlementsItem(SettlementsResponse settlementsItem) {
        if (this.settlements == null) {
            this.settlements = new ArrayList<SettlementsResponse>();
        }
        this.settlements.add(settlementsItem);
        return this;
    }

}
