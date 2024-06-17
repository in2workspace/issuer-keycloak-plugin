package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TokenResponseTest {

    @Test
    void testNoArgsConstructor() {
        TokenResponse tokenResponse = new TokenResponse();

        assertNull(tokenResponse.getAccessToken());
        assertNull(tokenResponse.getTokenType());
        assertNull(tokenResponse.getNonce());
    }

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

    @Test
    void testEqualsAndHashCode() {
        String accessToken1 = "accessToken1";
        String tokenType1 = "tokenType1";
        long expiresIn1 = 3600;
        String nonce1 = "nonce1";
        Long nonceExpiresIn1 = 600L;

        String accessToken2 = "accessToken2";
        String tokenType2 = "tokenType2";
        long expiresIn2 = 7200;
        String nonce2 = "nonce2";
        Long nonceExpiresIn2 = 1200L;

        TokenResponse tokenResponse1 = new TokenResponse(accessToken1, tokenType1, expiresIn1, nonce1, nonceExpiresIn1);
        TokenResponse tokenResponse2 = new TokenResponse(accessToken1, tokenType1, expiresIn1, nonce1, nonceExpiresIn1);
        TokenResponse tokenResponse3 = new TokenResponse(accessToken2, tokenType2, expiresIn2, nonce2, nonceExpiresIn2);

        assertEquals(tokenResponse1, tokenResponse2);
        assertEquals(tokenResponse1.hashCode(), tokenResponse2.hashCode());

        assertEquals(tokenResponse1, tokenResponse1);  // Reflexivity
        assertEquals(tokenResponse1.equals(tokenResponse2), tokenResponse2.equals(tokenResponse1));  // Symmetry
        assertEquals(tokenResponse1.equals(tokenResponse2) && tokenResponse2.equals(tokenResponse3), tokenResponse1.equals(tokenResponse3));  // Transitivity
        assertEquals(tokenResponse1.equals(tokenResponse2), tokenResponse1.equals(tokenResponse2));  // Consistency
    }

    @Test
    void testToString() {
        String accessToken = "accessToken";
        String tokenType = "tokenType";
        long expiresIn = 3600;
        String nonce = "nonce";
        Long nonceExpiresIn = 600L;

        TokenResponse tokenResponse = new TokenResponse(accessToken, tokenType, expiresIn, nonce, nonceExpiresIn);

        String expectedToString = "TokenResponse(accessToken=" + accessToken + ", tokenType=" + tokenType +
                ", expiresIn=" + expiresIn + ", nonce=" + nonce + ", nonceExpiresIn=" + nonceExpiresIn + ")";

        assertEquals(expectedToString, tokenResponse.toString());
    }
}
