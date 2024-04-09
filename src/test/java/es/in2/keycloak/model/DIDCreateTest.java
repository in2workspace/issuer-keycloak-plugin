package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static io.smallrye.common.constraint.Assert.assertNotNull;
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

    @Test
    void testNoArgsConstructor() {
        DIDCreate didCreate = new DIDCreate();

        assertNotNull(didCreate.getMethod());
        assertEquals("key", didCreate.getMethod());
    }

    @Test
    void testEqualsAndHashCode() {
        DIDCreate didCreate1 = new DIDCreate("value");
        DIDCreate didCreate2 = new DIDCreate("value");

        assertEquals(didCreate1, didCreate2);
        assertEquals(didCreate1.hashCode(), didCreate2.hashCode());
    }

    @Test
    void testToString() {
        DIDCreate didCreate = new DIDCreate("value");

        String expectedToString = "DIDCreate(method=value)";

        assertEquals(expectedToString, didCreate.toString());
    }
}
