package es.in2.keycloak.model;

import es.in2.keycloak.SIOP2Client;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SIOP2ClientTest {

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
}
