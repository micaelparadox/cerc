package com.cerc.tio.financial.asset.tr.layout.middleware.merchant.client;

import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(
    name = "merchant-client",
    url = "${cadastra.merchant.url}"
)
public interface MerchantClient {
    String X_API_TOKEN = "X-API-Token";

    @PostMapping("/vtag/merchant")
    ResponseEntity<List<MerchantResponse>> sendMerchant(
        @RequestHeader(AUTHORIZATION) String gkeReverseProxyAuth,
        @RequestHeader(X_API_TOKEN) String token,
        @RequestBody List<MerchantRequest> request
    );

}
