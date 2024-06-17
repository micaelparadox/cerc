package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.client;


import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.AuthRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@FeignClient(
    name = "auth-client",
    url = "${cadastra.auth.url}"
)
public interface AuthClient {
    String X_API_TOKEN = "X-API-Token";

    @GetMapping("/v1/auth/authorize/ar-ps")
    ResponseEntity<Object> autorizeArPs(
        @RequestHeader(AUTHORIZATION) String gkeReverseProxyAuth,
        @RequestHeader(X_API_TOKEN) String token,
        @SpringQueryMap AuthRequest request
    );

    // TODO: Use Feign Interface
    default Token authenticateApplication(
        String authUrl,
        String gkeReverseProxyAuth,
        String authorization
    ) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.set(AUTHORIZATION, gkeReverseProxyAuth);
        headers.set(X_API_TOKEN, authorization);

        var map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "client_credentials");

        var request = new HttpEntity<>(map, headers);
        return restTemplate.postForObject(authUrl + "/oauth/token", request, Token.class);
    }
}
