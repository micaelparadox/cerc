package com.cerc.tio.financial.asset.tr.layout.middleware.merchant.service;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.Token;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.service.AuthService;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.client.MerchantClient;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.merchant.domain.MerchantResponse;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private MerchantClient merchantClient;

    @InjectMocks
    private MerchantService underTest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "gkeReverseProxyAuth", "token");
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendMerchant_Should_return_merchant_response_When_authorized() {
        // Given
        var request = List.of(MerchantRequest.builder().build());
        Token token = buildToken();

        // When
        lenient().when(authService.getToken())
            .thenReturn(token);
        when(merchantClient.sendMerchant(anyString(), anyString(), any(List.class)))
            .thenReturn(ResponseEntity.accepted().body(List.of(new MerchantResponse())));

        List<MerchantResponse> merchantRequest = underTest.sendMerchant(request);

        // Then
        assertNotNull(merchantRequest);
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendMerchant_Should_throw_exception_When_unauthorized() {
        // Given
        var request = List.of(MerchantRequest.builder().build());
        Token token = buildToken();

        // When
        lenient().when(authService.getToken())
            .thenReturn(token);
        when(merchantClient.sendMerchant(anyString(), anyString(), any(List.class)))
            .thenThrow(new FeignException.Unauthorized(
                "Unauthorized",
                Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate()),
                "Unauthorized".getBytes(StandardCharsets.UTF_8),
                Map.of()));

        // Then
        assertThrows(FeignException.Unauthorized.class, () -> underTest.sendMerchant(request));
    }

    private Token buildToken() {
        return new Token(
            "token",
            "Bearer",
            13000L,
            "scope"
        );
    }
}