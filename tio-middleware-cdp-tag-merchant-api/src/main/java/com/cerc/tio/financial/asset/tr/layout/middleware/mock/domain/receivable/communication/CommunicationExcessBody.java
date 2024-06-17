package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.communication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunicationExcessBody {
    private String communicationType = "2";
    private String communicator = "99999999999999";
    private List<String> assetHolder = new ArrayList<String>();
    private String creditor = "99999999999999";
    private List<String> acquirers = null;
    private List<String> contractKeys = new ArrayList<String>();
    private List<String> paymentSchemes = null;
    private LocalDate startDate;
    private LocalDate endDate;
}
