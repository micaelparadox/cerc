package com.cerc.tio.libcdpcommon.domain.bookkeeping;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BKTransactionDomainType {
    CONTRACT("contract"),
    FINANCIAL_ASSET("asset"),
    ANTICIPATION("anticipation"),
    FINANCIAL_ASSET_TRANSACTIONS("financial_asset_transactions"),
    CONTRACT_TERMINATION_SOLICITATION("contract_termination_solicitation"),
    FINANCIAL_ASSET_SETTLEMENTS("financial_asset_settlements");

    private final String domain;
}
