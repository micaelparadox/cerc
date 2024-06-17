package com.cerc.tio.libcdpcommon.validation.enumeration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


class AccountTypeTest {

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void getAccountTypeByCode_Should_return_accountType_When_parameter_is_valid(AccountType accountType) {
        String code = accountType.getValue();
        Assertions.assertThat(code).isNotBlank();
    }
}