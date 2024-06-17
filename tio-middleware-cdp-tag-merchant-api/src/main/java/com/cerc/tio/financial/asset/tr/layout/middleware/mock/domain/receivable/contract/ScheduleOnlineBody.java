package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleOnlineBody {
        private String originalAssetHolder = "99999999999999";
        private String paymentScheme = "VCC";
        private String acquirer = "99999999999999";
        private LocalDate initialExpectedSettlementDate;
        private LocalDate finalExpectedSettlementDate;

    }
