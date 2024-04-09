package es.in2.keycloak;

import es.in2.keycloak.model.SupportedCredential;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SIOP2ClientTest {

    @Test
    void testNoArgsConstructor() {
        SIOP2Client client = new SIOP2Client();

        // Test fields initialized by no-args constructor
        assertNull(client.getClientDid());
        assertNull(client.getSupportedVCTypes());
        assertNull(client.getDescription());
        assertNull(client.getName());
        assertNull(client.getExpiryInMin());
        assertNull(client.getAdditionalClaims());
    }

    @Test
    void testAllArgsConstructor() {
        String clientDid = "client123";
        List<SupportedCredential> supportedVCTypes = List.of(new SupportedCredential("type1", null));
        String description = "Client description";
        String name = "Client name";
        Long expiryInMin = 60L;
        Map<String, String> additionalClaims = new HashMap<>();
        additionalClaims.put("claim1", "value1");

        SIOP2Client siop2Client = new SIOP2Client(clientDid, supportedVCTypes, description, name, expiryInMin, additionalClaims);

        assertEquals(clientDid, siop2Client.getClientDid());
        assertEquals(supportedVCTypes, siop2Client.getSupportedVCTypes());
        assertEquals(description, siop2Client.getDescription());
        assertEquals(name, siop2Client.getName());
        assertEquals(expiryInMin, siop2Client.getExpiryInMin());
        assertEquals(additionalClaims, siop2Client.getAdditionalClaims());
    }

    @Test
    void testGetterSetter() {
        SIOP2Client siop2Client = new SIOP2Client();

        String clientDid = "client123";
        List<SupportedCredential> supportedVCTypes = List.of(new SupportedCredential("type1", null));
        String description = "Client description";
        String name = "Client name";
        Long expiryInMin = 60L;
        Map<String, String> additionalClaims = new HashMap<>();
        additionalClaims.put("claim1", "value1");

        siop2Client.setClientDid(clientDid);
        siop2Client.setSupportedVCTypes(supportedVCTypes);
        siop2Client.setDescription(description);
        siop2Client.setName(name);
        siop2Client.setExpiryInMin(expiryInMin);
        siop2Client.setAdditionalClaims(additionalClaims);

        assertEquals(clientDid, siop2Client.getClientDid());
        assertEquals(supportedVCTypes, siop2Client.getSupportedVCTypes());
        assertEquals(description, siop2Client.getDescription());
        assertEquals(name, siop2Client.getName());
        assertEquals(expiryInMin, siop2Client.getExpiryInMin());
        assertEquals(additionalClaims, siop2Client.getAdditionalClaims());
    }

    @Test
    void testEqualsAndHashCode() {
        // Initialize two instances of SIOP2Client with identical field values
        SIOP2Client client1 = new SIOP2Client("clientDid", List.of(new SupportedCredential()), "description", "name", 60L, new HashMap<>());
        SIOP2Client client2 = new SIOP2Client("clientDid", List.of(new SupportedCredential()), "description", "name", 60L, new HashMap<>());

        // Test equals method
        assertEquals(client1, client2);
        assertEquals(client1.hashCode(), client2.hashCode());

        // Modify one field in client2
        client2.setName("modifiedName");

        // Test inequality after modification
        assertNotEquals(client1, client2);
        assertNotEquals(client1.hashCode(), client2.hashCode());
    }

    @Test
    void testToString() {
        // Initialize an instance of SIOP2Client
        SIOP2Client client = new SIOP2Client("clientDid", List.of(new SupportedCredential()), "description", "name", 60L, new HashMap<>());

        // Test toString method
        String expectedToString = "SIOP2Client(clientDid=clientDid, supportedVCTypes=[SupportedCredential(type=null, format=null)], " +
                "description=description, name=name, expiryInMin=60, additionalClaims={})";
        assertEquals(expectedToString, client.toString());
    }
}
