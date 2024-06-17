package com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantRequest {
    private String finalReceivingUser;
    private StatusId statusId;
    private TypeOfDocumentFinalReceivingUser typeOfDocumentFinalReceivingUser;
    private String typeOfFinalReceivingUser = "1";
    private TypeOfOperation typeOfOperation;
    private String companyOwnerId;
    private List<String> schemeList;
    private BankAccount bankAccount;
    private String externalReference;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum TypeOfDocumentFinalReceivingUser {
        CPF("1"),
        CNPJ("2");

        private String type;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum StatusId {
        ACTIVE("1"),
        INACTIVE("2");

        private String status;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BankAccount {
        private String agency;
        private String number;
        private String type;
        private String ispb;
        private String holderDocument;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum TypeOfOperation {
        REGISTER("C"),
        UPDATE("A");

        private String operation;
    }
}
