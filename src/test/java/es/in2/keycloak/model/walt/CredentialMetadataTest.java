package es.in2.keycloak.model.walt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
