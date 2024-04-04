package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
