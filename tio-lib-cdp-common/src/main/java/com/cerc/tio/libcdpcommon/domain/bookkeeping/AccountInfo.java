package com.cerc.tio.libcdpcommon.domain.bookkeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    private String holderDocument;
    private AccountType type;
    private String ispb;
    private String agency;
    private String number;


    public enum AccountType{
        BANK_ACCOUNT,
        DEPOSIT_ACCOUNT,
        COLLATERAL_ACCOUNT,
        INVESTMENT_ACCOUNT,
        PAYMENT_ACCOUNT,
        SAVINGS_ACCOUNT;

        public static AccountType getTypeByCode(String value){

            return switch (value) {
                case "CC" -> AccountType.BANK_ACCOUNT;
                case "CD" -> AccountType.DEPOSIT_ACCOUNT;
                case "CG" -> AccountType.COLLATERAL_ACCOUNT;
                case "CI" -> AccountType.INVESTMENT_ACCOUNT;
                case "PG" -> AccountType.PAYMENT_ACCOUNT;
                case "PP" -> AccountType.SAVINGS_ACCOUNT;
                default -> null;
            };
        }
    }

}


