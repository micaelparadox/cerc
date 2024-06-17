package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain;

import lombok.Builder;

@Builder
public class AuthRequest {
    private String arDocument;
    private String psDocument;
    private String processCode;
    private String walletCode;
}
