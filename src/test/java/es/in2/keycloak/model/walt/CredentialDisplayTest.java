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

}
