package es.in2.keycloak.model.walt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CredentialDisplayTest {

    @Test
    void testAllArgsConstructor() {
        CredentialDisplay credentialDisplay = new CredentialDisplay("TestName");
        assertEquals("TestName", credentialDisplay.getName());
    }

    @Test
    void testGettersAndSetters() {
        CredentialDisplay credentialDisplay = new CredentialDisplay();
        credentialDisplay.setName("NewName");
        assertEquals("NewName", credentialDisplay.getName());
    }

    @Test
    void testNoArgsConstructor() {
        CredentialDisplay credentialDisplay = new CredentialDisplay();

        assertNull(credentialDisplay.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        CredentialDisplay credentialDisplay1 = new CredentialDisplay("example");
        CredentialDisplay credentialDisplay2 = new CredentialDisplay("example");

        assertEquals(credentialDisplay1, credentialDisplay2);
        assertEquals(credentialDisplay1.hashCode(), credentialDisplay2.hashCode());
    }

    @Test
    void testToString() {
        CredentialDisplay credentialDisplay = new CredentialDisplay("example");

        String expectedToString = "CredentialDisplay(name=example)";

        assertEquals(expectedToString, credentialDisplay.toString());
    }

}
