package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.AuthRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.Token;

class AuthClientTest {

	@Mock
	private RestTemplate restTemplate;

	private AuthClient authClient;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		authClient = new AuthClient() {
			@Override
			public Token fetchToken(String authUrl, String authorization) {
				var headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				headers.set(HttpHeaders.AUTHORIZATION, authorization);

				var map = new LinkedMultiValueMap<String, String>();
				map.add("grant_type", "client_credentials");

				var request = new HttpEntity<>(map, headers);
				return restTemplate.postForObject(authUrl + "/oauth/token", request, Token.class);
			}

			@Override
			public ResponseEntity<Object> autorizeArPs(String token, AuthRequest request) {
				return null;
			}
		};
	}

	@Test
	void testFetchToken() {
		// Arrange
		String authUrl = "http://test-auth-url";
		String authorization = "Basic dGVzdDp0ZXN0";
		Token expectedToken = new Token("accessToken", "bearer", 3600L, "scope");

		when(restTemplate.postForObject(eq(authUrl + "/oauth/token"), any(HttpEntity.class), eq(Token.class)))
				.thenReturn(expectedToken);

		// Act
		Token actualToken = authClient.fetchToken(authUrl, authorization);

		// Assert
		assertEquals(expectedToken.getAccessToken(), actualToken.getAccessToken());
		assertEquals(expectedToken.getTokenType(), actualToken.getTokenType());
		assertEquals(expectedToken.getExpiresIn(), actualToken.getExpiresIn());
		assertEquals(expectedToken.getScope(), actualToken.getScope());
	}

	@Test
	void testFetchTokenWithInvalidUrl() {
		// Arrange
		String authUrl = "http://invalid-url";
		String authorization = "Basic dGVzdDp0ZXN0";

		when(restTemplate.postForObject(eq(authUrl + "/oauth/token"), any(HttpEntity.class), eq(Token.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

		// Act & Assert
		assertThrows(HttpClientErrorException.class, () -> authClient.fetchToken(authUrl, authorization));
	}

	@Test
	void testFetchTokenWithInvalidAuthorization() {
		// Arrange
		String authUrl = "http://test-auth-url";
		String authorization = "InvalidAuthorization";
		Token expectedToken = new Token("accessToken", "bearer", 3600L, "scope");

		when(restTemplate.postForObject(eq(authUrl + "/oauth/token"), any(HttpEntity.class), eq(Token.class)))
				.thenReturn(expectedToken);

		// Act
		Token actualToken = authClient.fetchToken(authUrl, authorization);

		// Assert
		assertEquals(expectedToken.getAccessToken(), actualToken.getAccessToken());
		assertEquals(expectedToken.getTokenType(), actualToken.getTokenType());
		assertEquals(expectedToken.getExpiresIn(), actualToken.getExpiresIn());
		assertEquals(expectedToken.getScope(), actualToken.getScope());
	}

	@Test
	void testFetchTokenWithRestTemplateError() {
		// Arrange
		String authUrl = "http://test-auth-url";
		String authorization = "Basic dGVzdDp0ZXN0";

		when(restTemplate.postForObject(eq(authUrl + "/oauth/token"), any(HttpEntity.class), eq(Token.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

		// Act & Assert
		assertThrows(HttpClientErrorException.class, () -> authClient.fetchToken(authUrl, authorization));
	}
}
