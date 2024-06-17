package com.cerc.tio.libcdpcommon.domain.bookkeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    private static final String CERC_DOCUMENT = "23399607000191";

    private String contractId;
    private Type type;
    private String reference;
    private String participantContractIdentifier;
    private EffectType effectType;
    private OperationType operationType;
    @Builder.Default
    private final String tradeRepositoryHash = CERC_DOCUMENT;
    private String signatureDate;
    private String expirationDate;
    private String contractorHash;
    private String originalRequesterHash;
    private String ownerHash;
    private BigDecimal value;
    private BigDecimal maxCollateralValue;
    private BigDecimal minCollateralValue;
    private String trManagesCollateral;

    private List<EffectFilter> effectFilters;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EffectFilter {
        private String externalReference;
        private String assetOwnerDocumentHash;
        private ValueType valueType;
        private BigDecimal value;
        private AccountInfo accountInfo;
        private String assetSettlementStartDate;
        private String assetSettlementEndDate;
        private List<String> schemes;
        private List<String> payees;
        private List<String> payers;

        private List<ContractEffectAsset> assets;

        @Getter
        @NoArgsConstructor
        public enum ValueType {
            FIXED,
            PERCENTUAL;
        }


    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractEffectAsset {
        private String assetCommitmentHash;
        private BigDecimal restrictedValue;
        private Boolean active;
    }

    @Getter
    @NoArgsConstructor
    public enum Type {
        CONTRACT,
        ANTECIPATION;
    }

    @Getter
    @NoArgsConstructor
    public enum EffectType {
        OWNERSHIP_CHANGE,
        FIDUCIARY_CESSION,
        ONUS,
        JUDICIAL_FREEZE;
    }

    @Getter
    @NoArgsConstructor
    public enum OperationType {
        INSTALLMENT,
        REVOLVING,
        CESSION;
    }

}
