package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ErrorResponseTest {

    @Test
    void testAllArgsConstructor() {
        ErrorResponse errorResponse = new ErrorResponse("error message");
        assertEquals("error message", errorResponse.getError());
    }

    @Test
    void testGetterAndSetter() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("new error message");

        assertEquals("new error message", errorResponse.getError());
    }

    @Test
    void testNoArgsConstructor() {
        ErrorResponse errorResponse = new ErrorResponse();

        assertNull(errorResponse.getError());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorResponse errorResponse1 = new ErrorResponse("error message");
        ErrorResponse errorResponse2 = new ErrorResponse("error message");

        assertEquals(errorResponse1, errorResponse2);
        assertEquals(errorResponse1.hashCode(), errorResponse2.hashCode());
    }

    @Test
    void testToString() {
        ErrorResponse errorResponse = new ErrorResponse("error message");

        String expectedToString = "ErrorResponse(error=error message)";

        assertEquals(expectedToString, errorResponse.toString());
    }
}
