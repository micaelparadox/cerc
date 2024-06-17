package com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantResponse {
    private String protocol;
    private String finalReceivingUser;
    private String companyOnwerId;
    private int status;
    private String createdAt;
    private String updatedAt;
    private String externalReference;
    private List<Error> errors;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String code;
        private String message;
    }
}
