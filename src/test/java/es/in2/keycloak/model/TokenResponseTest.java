package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenResponseTest {

    @Test
    void testAllArgsConstructor() {
        TokenResponse tokenResponse = new TokenResponse("accessTokenValue", "tokenTypeValue", 3600L, "nonceValue", 600L);

        assertEquals("accessTokenValue", tokenResponse.getAccessToken());
        assertEquals("tokenTypeValue", tokenResponse.getTokenType());
        assertEquals(3600L, tokenResponse.getExpiresIn());
        assertEquals("nonceValue", tokenResponse.getNonce());
        assertEquals(600L, tokenResponse.getNonceExpiresIn());
    }

    @Test
    void testGetterSetter() {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("newAccessToken");
        tokenResponse.setTokenType("newTokenType");
        tokenResponse.setExpiresIn(7200L);
        tokenResponse.setNonce("newNonce");
        tokenResponse.setNonceExpiresIn(900L);

        assertEquals("newAccessToken", tokenResponse.getAccessToken());
        assertEquals("newTokenType", tokenResponse.getTokenType());
        assertEquals(7200L, tokenResponse.getExpiresIn());
        assertEquals("newNonce", tokenResponse.getNonce());
        assertEquals(900L, tokenResponse.getNonceExpiresIn());
    }
}
