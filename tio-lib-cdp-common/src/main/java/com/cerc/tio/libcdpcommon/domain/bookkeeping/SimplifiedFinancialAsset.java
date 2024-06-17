package com.cerc.tio.libcdpcommon.domain.bookkeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedFinancialAsset {
    private static final String CERC_DOCUMENT = "23399607000191";

    private String financialAssetId;

    @Builder.Default
    private String commitmentHash = "";

    @Builder.Default
    private final String type = "SIMPLIFIED_FINAL_RECEIVER_PAYMENT_SCHEME";
    private String payer;
    private String originalOwner;
    private String owner;
    private String schemeCode;
    private String expectedSettlementDate;
    private String externalReference;
    @Builder.Default
    private final String tradeRepositoryHash = CERC_DOCUMENT;
    private BigDecimal expectedSettlementValue;
    private BigDecimal actualValue;
    private BigDecimal preNegotiatedValue;
    private List<Party> parties;
    private List<Settlement> settlements;

    @PostConstruct
    private void init() {
        this.commitmentHash = String.format("%s_%s_%s_%s_%s",this.originalOwner, this.originalOwner, this.payer, this.schemeCode, this.expectedSettlementDate);
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Party {
        private String financialAssetId;
        private String documentHash;
        @Builder.Default
        private String type = "ORIGINAL_OWNER";
        @Builder.Default
        private BigDecimal participationQuantity = new BigDecimal("100.00");
        @Builder.Default
        private String participationType = "PERCENTUAL_VALUE";
        private List<AccountInfo> paymentInfos;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Settlement {
        private BigDecimal value;
        private String date;
        private String assetOwner;
        private String reference;
        private AccountInfo accountInfo;
    }

}
