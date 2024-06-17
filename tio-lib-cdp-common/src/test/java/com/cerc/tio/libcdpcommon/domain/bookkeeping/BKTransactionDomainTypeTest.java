package com.cerc.tio.libcdpcommon.domain.bookkeeping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BKTransactionDomainTypeTest {

    @Test
    void getDomain_shouldReturnItsRightName() {
        assertEquals("contract", BKTransactionDomainType.CONTRACT.getDomain());
        assertEquals("asset", BKTransactionDomainType.FINANCIAL_ASSET.getDomain());
        assertEquals("anticipation", BKTransactionDomainType.ANTICIPATION.getDomain());
        assertEquals("financial_asset_transactions", BKTransactionDomainType.FINANCIAL_ASSET_TRANSACTIONS.getDomain());
        assertEquals("contract_termination_solicitation", BKTransactionDomainType.CONTRACT_TERMINATION_SOLICITATION.getDomain());
        assertEquals("financial_asset_settlements", BKTransactionDomainType.FINANCIAL_ASSET_SETTLEMENTS.getDomain());
    }
}