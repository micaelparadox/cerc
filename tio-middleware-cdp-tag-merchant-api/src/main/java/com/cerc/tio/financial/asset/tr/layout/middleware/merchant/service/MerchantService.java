package com.cerc.tio.financial.asset.tr.layout.middleware.merchant.service;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.service.AuthService;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.client.MerchantClient;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantService.class);

    @Value("${cadastra.reverse-proxy-auth-secret}")
    private String gkeReverseProxyAuth;

    private final MerchantClient merchantClient;
    private final AuthService authService;

    @SuppressWarnings("squid:S2139")
    public List<MerchantResponse> sendMerchant(final List<MerchantRequest> request) throws FeignException {
        try {
            final String accessToken = authService.getToken().getAccessToken();
            return merchantClient.sendMerchant(gkeReverseProxyAuth, accessToken, request).getBody();
        } catch (FeignException e) {
            LOGGER.error("Error authorizing request: {}", e.contentUTF8());
            throw e;
        }
    }

}
