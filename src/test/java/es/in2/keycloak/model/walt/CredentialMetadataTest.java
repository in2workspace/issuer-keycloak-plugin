package es.in2.keycloak.model.walt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class CredentialMetadataTest {
    @Test
    void testAllArgsConstructor() {
        Map<String, FormatObject> formats = new HashMap<>();
        formats.put("format1", new FormatObject());
        List<CredentialDisplay> display = new ArrayList<>();
        display.add(new CredentialDisplay("TestName"));
        CredentialMetadata credentialMetadata = new CredentialMetadata(formats, display);
        assertEquals(formats, credentialMetadata.getFormats());
        assertEquals(display, credentialMetadata.getDisplay());
    }

    @Test
    void testGettersAndSetters() {
        Map<String, FormatObject> formats = new HashMap<>();
        formats.put("format1", new FormatObject());
        List<CredentialDisplay> display = new ArrayList<>();
        display.add(new CredentialDisplay("TestName"));

        CredentialMetadata credentialMetadata = new CredentialMetadata();

        credentialMetadata.setFormats(formats);
        credentialMetadata.setDisplay(display);

        assertEquals(formats, credentialMetadata.getFormats());
        assertEquals(display, credentialMetadata.getDisplay());
    }

    @Test
    void testNoArgsConstructor() {
        CredentialMetadata credentialMetadata = new CredentialMetadata();

        assertNull(credentialMetadata.getFormats());
        assertNull(credentialMetadata.getDisplay());
    }

    @Test
    void testEqualsAndHashCode() {
        Map<String, FormatObject> formats = new HashMap<>();
        formats.put("format1", new FormatObject(Collections.singletonList("format1")));

        List<CredentialDisplay> display = new ArrayList<>();
        display.add(new CredentialDisplay("example"));

        CredentialMetadata credentialMetadata1 = new CredentialMetadata(formats, display);
        CredentialMetadata credentialMetadata2 = new CredentialMetadata(formats, display);

        assertEquals(credentialMetadata1, credentialMetadata2);
        assertEquals(credentialMetadata1.hashCode(), credentialMetadata2.hashCode());
    }

    @Test
    void testToString() {
        Map<String, FormatObject> formats = new HashMap<>();
        formats.put("format1", new FormatObject(Collections.singletonList("format1")));

        List<CredentialDisplay> display = new ArrayList<>();
        display.add(new CredentialDisplay("example"));

        CredentialMetadata credentialMetadata = new CredentialMetadata(formats, display);

        String expectedToString = "CredentialMetadata(formats=" + formats + ", display=" + display + ")";

        assertEquals(expectedToString, credentialMetadata.toString());
    }

}
