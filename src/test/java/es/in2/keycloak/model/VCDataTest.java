package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VCDataTest {

    @Test
    void testAllArgsConstructor() {
        VCClaims vcClaims = new VCClaims();
        vcClaims.setFirstName("John");
        vcClaims.setFamilyName("Doe");

        VCData vcData = new VCData(vcClaims);

        assertNotNull(vcData.getCredentialSubject());
        assertEquals("John", vcData.getCredentialSubject().getFirstName());
        assertEquals("Doe", vcData.getCredentialSubject().getFamilyName());
    }

    @Test
    void testBuilder() {
        VCClaims vcClaims = new VCClaims();
        vcClaims.setFirstName("John");
        vcClaims.setFamilyName("Doe");

        VCData vcData = VCData.builder()
                .credentialSubject(vcClaims)
                .build();

        assertNotNull(vcData.getCredentialSubject());
        assertEquals("John", vcData.getCredentialSubject().getFirstName());
        assertEquals("Doe", vcData.getCredentialSubject().getFamilyName());
    }

    @Test
    void testGetterSetter() {
        VCData vcData = new VCData();

        VCClaims vcClaims = new VCClaims();
        vcClaims.setFirstName("John");
        vcClaims.setFamilyName("Doe");

        vcData.setCredentialSubject(vcClaims);

        assertNotNull(vcData.getCredentialSubject());
        assertEquals("John", vcData.getCredentialSubject().getFirstName());
        assertEquals("Doe", vcData.getCredentialSubject().getFamilyName());
    }
}

