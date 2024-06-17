package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TokenTest {
    @Test
    void testGetNotExpiredAccessToken() throws JsonProcessingException {

        var token = new Token(
                null,
                null,
                13000L,
                null);
        assertFalse(token.isExpired());

    }

    @Test
    void testGetExpiredAccessToken() throws JsonProcessingException {

        var token = new Token(
                "token",
                "Bearer",
                -13000L,
                "scope");

        assertTrue(token.isExpired());

    }

    @Test
    void testGetJsonNotExpiredAccessToken() throws JsonProcessingException {

        var token = new Token(
                "token",
                "Bearer",
                -1300L,
                "scope");
        var now = Instant.now().plusSeconds(-1300L);
        var instant_seconds = now.getEpochSecond();
        System.out.println(instant_seconds);

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        var result = mapper.writeValueAsString(token);
        System.out.println(result);

        assertTrue(result.contains("\"scope\":\"scope\""));
        assertTrue(result.contains("\"accessToken\":\"Bearer token\""));
        assertTrue(result.contains("\"tokenType\":\"Bearer\""));
        assertTrue(result.contains("\"expiresIn\":-1300"));
        assertTrue(result.contains(String.format("\"expiresAt\":%d.", instant_seconds)));
        assertTrue(result.contains("\"expired\":true"));

    }

    @Test
    void testGetJsonExpiredAccessToken() throws JsonProcessingException {

        var token = new Token(
                "token",
                "Bearer",
                1300L,
                "scope");
        var now = Instant.now().plusSeconds(1300L);
        var instant_seconds = now.getEpochSecond();
        System.out.println(instant_seconds);

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        var result = mapper.writeValueAsString(token);
        System.out.println(result);

        assertTrue(result.contains("\"scope\":\"scope\""));
        assertTrue(result.contains("\"accessToken\":\"Bearer token\""));
        assertTrue(result.contains("\"tokenType\":\"Bearer\""));
        assertTrue(result.contains("\"expiresIn\":1300"));
        assertTrue(result.contains(String.format("\"expiresAt\":%d.", instant_seconds)));
        assertTrue(result.contains("\"expired\":false"));

    }

}
