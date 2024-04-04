package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DIDCreateTest {

    @Test
    void testConstructor() {
        DIDCreate didCreate1 = new DIDCreate("key");
        assertEquals("key", didCreate1.getMethod());

        DIDCreate didCreate2 = new DIDCreate();
        assertEquals("key", didCreate2.getMethod()); // Method should default to "key"
    }

    @Test
    void testGetterAndSetter() {
        DIDCreate didCreate = new DIDCreate();
        didCreate.setMethod("newMethod");
        assertEquals("newMethod", didCreate.getMethod());
    }
}
