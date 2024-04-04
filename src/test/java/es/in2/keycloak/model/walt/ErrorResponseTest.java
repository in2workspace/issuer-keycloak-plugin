package es.in2.keycloak.model.walt;

import es.in2.keycloak.model.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {

    @Test
    public void testAllArgsConstructor() {
        ErrorResponse errorResponse = new ErrorResponse("error message");
        assertEquals("error message", errorResponse.getError());
    }

    @Test
    public void testGetterAndSetter() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("new error message");

        assertEquals("new error message", errorResponse.getError());
    }
}
