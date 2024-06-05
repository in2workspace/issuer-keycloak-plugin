package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    void testNoArgsConstructor() {
        KeyId keyId = new KeyId();

        assertNull(keyId.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        KeyId keyId1 = new KeyId("id1");
        KeyId keyId2 = new KeyId("id1");
        KeyId keyId3 = new KeyId("id2");

        assertEquals(keyId1, keyId2);
        assertEquals(keyId1.hashCode(), keyId2.hashCode());

        assertEquals(keyId1, keyId1);  // Reflexivity
        assertEquals(keyId1.equals(keyId2), keyId2.equals(keyId1));  // Symmetry
        assertEquals(keyId1.equals(keyId2) && keyId2.equals(keyId3), keyId1.equals(keyId3));  // Transitivity
        assertEquals(keyId1.equals(keyId2), keyId1.equals(keyId2));  // Consistency
    }

    @Test
    void testToString() {
        KeyId keyId = new KeyId("exampleId");

        assertEquals("KeyId(id=exampleId)", keyId.toString());
    }
}
