package com.cerc.tio.libcdpcommon.domain.bookkeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BKNotificationMessage {
    private String processingID;
    private String requesterTransactionID;
    private String affectedResourceID;
    private String reference;
    private String domainType;
    private String status;
    private List<Error> errors;
    private List<Settlement> settlements;
    private List<EffectFilter> effectFilters;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String errorCode;
        private String errorReason;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Settlement {
        private String id;
        private String reference;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EffectFilter {
        private String id;
        private List<Asset> assets;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Asset {
            private String commitmentKey;
            private boolean constituted;
            private int totalConstituted;
            private int restrictedValue;
            private int assetValueAffected;
            private int blockedValue;
            private int onusPriority;
            private String tradeRepository;
            private List<PaymentInfo> paymentInfos;

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class PaymentInfo {
                private String holderDocument;
                private String holderName;
                private String type;
                private String ispb;
                private String agency;
                private String number;
                private String compe;
                private String settlementDate;
                private int expectedValue;
            }
        }
    }
}
