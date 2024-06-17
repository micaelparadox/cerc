package com.cerc.tio.libcdpcommon.validation.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType implements ValuedEnum {
    BANK_ACCOUNT ("CC"),
    DEPOSIT_ACCOUNT ("CD"),
    PAYMENT_ACCOUNT ("PG"),
    SAVINGS_ACCOUNT ("PP"),
    COLLATERAL_ACCOUNT("CG"),
    INVESTMENT_ACCOUNT("CI");

    private final String value;
}
