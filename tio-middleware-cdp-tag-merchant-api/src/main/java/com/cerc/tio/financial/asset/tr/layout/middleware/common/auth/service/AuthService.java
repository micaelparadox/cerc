package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.service;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.client.AuthClient;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.AuthRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.Token;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final AtomicReference<Token> TOKEN_CACHE = new AtomicReference<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Value("${cadastra.reverse-proxy-auth-secret}")
    private String gkeReverseProxyAuth;
    @Value("${cadastra.auth.app-authentication-basic-auth-secret}")
    private String basicAuth;
    @Value("${cadastra.auth.url}")
    private String authUrl;

    private final AuthClient authClient;

    public Token getToken() {
        return TOKEN_CACHE.updateAndGet(token -> {
            if (token == null || token.isExpired()) {
                return authClient.authenticateApplication(authUrl, gkeReverseProxyAuth, basicAuth);
            }
            return token;
        });
    }

    /**
     * This method is used to authorize a request using the provided token.
     * It sends the request to the {@link com.cerc.tio.financial.asset.tr.layout.middleware.merchant.client.MerchantClient}
     * and returns a pair of values.
     * The first value in the pair is a boolean indicating whether the authorization was successful.
     * The second value is a string containing the response body if the authorization was successful, or the error message if it was not.
     *
     * @param request The {@link AuthRequest} object containing the details of the request to be authorized.
     * @return A pair of values. The first value is a boolean indicating whether the authorization was successful.
     * The second value is a string containing the response body if the authorization was successful, or the error message if it was not.
     */
    public Pair<Boolean, String> authorizeParticipant(AuthRequest request) {
        try {
            final String accessToken = getToken().getAccessToken();
            ResponseEntity<Object> response = authClient.autorizeArPs(gkeReverseProxyAuth, accessToken, request);
            return Pair.of(
                response.getStatusCode().is2xxSuccessful(),
                Optional.ofNullable(response.getBody())
                    .map(Objects::toString)
                    .orElse("Authorized")
            );
        } catch (FeignException e) {
            LOGGER.error("Error authorizing request: {}", e.contentUTF8());
            return Pair.of(false, e.contentUTF8());
        }
    }
}
