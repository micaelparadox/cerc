package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Token {

    private final String accessToken;
    private final String tokenType;
    private final Long expiresIn;
    private final String scope;
    private final Instant expiresAt;

    @JsonCreator
    public Token(@JsonProperty("access_token") String accessToken,
                 @JsonProperty("token_type") String tokenType,
                 @JsonProperty("expires_in") Long expiresIn,
                 @JsonProperty("scope") String scope) {
        this.accessToken = "Bearer " + accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.expiresAt = Instant.now().plusSeconds(expiresIn);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
