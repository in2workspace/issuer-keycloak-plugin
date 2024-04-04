package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyIdTest {

    @Test
    void testAllArgsConstructor() {
        KeyId keyId = new KeyId("testId");
        assertEquals("testId", keyId.getId());
    }

    @Test
    void testGetterSetter() {
        KeyId keyId = new KeyId();
        keyId.setId("newId");
        assertEquals("newId", keyId.getId());
    }
}
