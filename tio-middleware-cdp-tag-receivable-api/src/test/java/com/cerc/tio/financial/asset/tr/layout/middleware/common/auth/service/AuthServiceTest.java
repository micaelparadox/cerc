package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.service;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.client.AuthClient;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.AuthRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.Token;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AuthService underTest;

    @Test
    void autorizeArPs_Should_return_true_and_sucess_When_authorized() {
        // Given
        var request = AuthRequest.builder().build();
        Token token = buildToken();

        // When
        lenient().when(authClient.fetchToken(any(), any()))
            .thenReturn(token);
        when(authClient.autorizeArPs(token.getAccessToken(), request))
            .thenReturn(ResponseEntity.accepted().build());
        Pair<Boolean, String> booleanStringPair = underTest.autorizeArPs(request);

        // Then
        assertTrue(booleanStringPair.getLeft());
    }

    @Test
    void autorizeArPs_Should_return_false_and_error_message_When_unauthorized() {
        // Given
        var request = AuthRequest.builder().build();
        Token token = buildToken();
        String errorResponse = """
            {"error": "Unauthorized"}
            """;

        // When
        lenient().when(authClient.fetchToken(any(), any()))
            .thenReturn(token);
        when(authClient.autorizeArPs(token.getAccessToken(), request))
            .thenThrow(new FeignException.Unauthorized(
                "Unauthorized",
                Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate()),
                errorResponse.getBytes(StandardCharsets.UTF_8),
                Map.of()));
        Pair<Boolean, String> booleanStringPair = underTest.autorizeArPs(request);

        // Then
        assertFalse(booleanStringPair.getLeft());
        assertEquals(errorResponse, booleanStringPair.getRight());
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