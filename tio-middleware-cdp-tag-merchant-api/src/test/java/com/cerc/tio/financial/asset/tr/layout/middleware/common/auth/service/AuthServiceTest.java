package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.service;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.client.AuthClient;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.AuthRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.Token;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AuthService underTest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "gkeReverseProxyAuth", "token");
        ReflectionTestUtils.setField(underTest, "authUrl", "url");
        ReflectionTestUtils.setField(underTest, "basicAuth", "auth");
    }

    @Test
    void autorizeArPs_Should_return_true_and_sucess_When_authorized() {
        // Given
        var request = AuthRequest.builder().build();
        Token token = buildToken();

        // When
        lenient().when(authClient.authenticateApplication(anyString(), anyString(), anyString()))
            .thenReturn(token);
        when(authClient.autorizeArPs(anyString(), anyString(), any(AuthRequest.class)))
            .thenReturn(ResponseEntity.accepted().build());
        Pair<Boolean, String> booleanStringPair = underTest.authorizeParticipant(request);

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
        lenient().when(authClient.authenticateApplication(anyString(), anyString(), anyString()))
            .thenReturn(token);
        when(authClient.autorizeArPs(anyString(), anyString(), any(AuthRequest.class)))
            .thenThrow(new FeignException.Unauthorized(
                "Unauthorized",
                Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate()),
                errorResponse.getBytes(StandardCharsets.UTF_8),
                Map.of()));
        Pair<Boolean, String> booleanStringPair = underTest.authorizeParticipant(request);

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