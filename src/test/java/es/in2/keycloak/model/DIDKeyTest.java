package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DIDKeyTest {

    @Test
    void testAllArgsConstructor() {
        // Test constructor with all arguments
        DIDKey didKey = new DIDKey("kty", "d", "use", "crv", "kid", "x", "alg");
        assertEquals("kty", didKey.getKty());
        assertEquals("d", didKey.getD());
        assertEquals("use", didKey.getUse());
        assertEquals("crv", didKey.getCrv());
        assertEquals("kid", didKey.getKid());
        assertEquals("x", didKey.getX());
        assertEquals("alg", didKey.getAlg());
    }

    @Test
    void testGetterAndSetter() {
        // Test setter and getter for each field
        DIDKey didKey = new DIDKey();
        didKey.setKty("newKty");
        didKey.setD("newD");
        didKey.setUse("newUse");
        didKey.setCrv("newCrv");
        didKey.setKid("newKid");
        didKey.setX("newX");
        didKey.setAlg("newAlg");

        assertEquals("newKty", didKey.getKty());
        assertEquals("newD", didKey.getD());
        assertEquals("newUse", didKey.getUse());
        assertEquals("newCrv", didKey.getCrv());
        assertEquals("newKid", didKey.getKid());
        assertEquals("newX", didKey.getX());
        assertEquals("newAlg", didKey.getAlg());
    }

    @Test
    void testNoArgsConstructor() {
        DIDKey didKey = new DIDKey();

        assertNull(didKey.getKty());
        assertNull(didKey.getD());
        assertNull(didKey.getUse());
        assertNull(didKey.getCrv());
        assertNull(didKey.getKid());
        assertNull(didKey.getX());
        assertNull(didKey.getAlg());
    }

    @Test
    void testToString() {
        DIDKey didKey = new DIDKey("kty", "d", "use", "crv", "kid", "x", "alg");

        String expectedToString = "DIDKey(kty=kty, d=d, use=use, crv=crv, kid=kid, x=x, alg=alg)";

        assertEquals(expectedToString, didKey.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two DIDKey objects with identical field values
        DIDKey didKey1 = new DIDKey("kty1", "d1", "use1", "crv1", "kid1", "x1", "alg1");
        DIDKey didKey2 = new DIDKey("kty1", "d1", "use1", "crv1", "kid1", "x1", "alg1");

        // Create two DIDKey objects with different field values
        DIDKey didKey3 = new DIDKey("kty2", "d2", "use2", "crv2", "kid2", "x2", "alg2");

        // Test for equality between identical objects
        assertEquals(didKey1, didKey2);
        assertEquals(didKey1.hashCode(), didKey2.hashCode());

        // Test for inequality between different objects
        assertNotEquals(didKey1, didKey3);
        assertNotEquals(didKey1.hashCode(), didKey3.hashCode());

        // Test for reflexivity (an object should equal itself)
        assertEquals(didKey1, didKey1);

        // Test for symmetry (if A equals B, then B should equal A)
        assertEquals(didKey1.equals(didKey2), didKey2.equals(didKey1));

        // Test for transitivity (if A equals B and B equals C, then A should equal C)
        DIDKey didKey4 = new DIDKey("kty1", "d1", "use1", "crv1", "kid1", "x1", "alg1");
        assertEquals(didKey1.equals(didKey2) && didKey2.equals(didKey4), didKey1.equals(didKey4));

        // Test for consistency (multiple calls to equals() should return the same result)
        assertEquals(didKey1, didKey2);
        assertEquals(didKey1, didKey2);
    }
}
