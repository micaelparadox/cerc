package com.cerc.tio.libcdpcommon.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static java.util.Optional.ofNullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormatterUtil {

    private static final int ONE_HUNDRED = 100;


    public static BigDecimal formatValue(String prePaidAmount){
        return ofNullable(prePaidAmount)
                .map(amount -> new BigDecimal(amount).divide(new BigDecimal(ONE_HUNDRED), 2, RoundingMode.HALF_EVEN))
                .orElse(null);
    }

    public static BigDecimal formatValue(BigInteger prePaidAmount){
        return ofNullable(prePaidAmount)
                .map(amount -> new BigDecimal(amount).divide(new BigDecimal(ONE_HUNDRED), 2, RoundingMode.HALF_EVEN))
                .orElse(null);
    }

    public static BigDecimal formatValue(BigInteger prePaidAmount, Boolean nullable){
        return ofNullable(prePaidAmount)
                .map(amount -> new BigDecimal(amount).divide(new BigDecimal(ONE_HUNDRED), 2, RoundingMode.HALF_EVEN))
                .orElse(nullable ? null : new BigDecimal(0));
    }


}
