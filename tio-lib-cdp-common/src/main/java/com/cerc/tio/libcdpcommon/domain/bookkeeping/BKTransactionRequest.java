package com.cerc.tio.libcdpcommon.domain.bookkeeping;

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
public class BKTransactionRequest {
    private String id;

    private String requesterTransactionId;
    private String type;
    private String requester;
    private String receivedAt;
    @Builder.Default
    private List<BKNotificationConfig> notifications = new ArrayList<>();
    private SimplifiedFinancialAsset financialAsset;
    private Contract contract;
    private List<FinancialAssetSettlement> financialAssetSettlements;

}
