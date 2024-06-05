package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorTypeTest {

    @Test
    void testAllArgsConstructor() {
        ErrorType errorType = ErrorType.INVALID_REQUEST;
        assertEquals("invalid_request", errorType.getValue());
    }

    @Test
    void testGetter() {
        ErrorType errorType = ErrorType.INVALID_TOKEN;
        assertEquals("invalid_token", errorType.getValue());
    }
}
