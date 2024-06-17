package com.cerc.tio.libcdpcommon.domain.bookkeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialAssetSettlement {
    private String settlementId;
    private String assetCommitmentHash;
    private String reference;
    private BigDecimal value;
    private String date;
    private String assetOwner;
    private AccountInfo accountInfo;

}
