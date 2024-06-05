package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VCDataTest {

    @Test
    void testNoArgsConstructor() {
        VCData vcData = new VCData();

        // Test fields initialized by no-args constructor
        assertNull(vcData.getCredentialSubject());
    }

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

    @Test
    void testEqualsAndHashCode() {
        VCClaims credentialSubject1 = new VCClaims();
        VCClaims credentialSubject2 = new VCClaims();

        VCData vcData1 = new VCData(credentialSubject1);
        VCData vcData2 = new VCData(credentialSubject1);
        VCData vcData3 = new VCData(credentialSubject2);

        assertEquals(vcData1, vcData2);
        assertEquals(vcData1.hashCode(), vcData2.hashCode());

        assertEquals(vcData1, vcData1);  // Reflexivity
        assertEquals(vcData1.equals(vcData2), vcData2.equals(vcData1));  // Symmetry
        assertEquals(vcData1.equals(vcData2) && vcData2.equals(vcData3), vcData1.equals(vcData3));  // Transitivity
        assertEquals(vcData1.equals(vcData2), vcData1.equals(vcData2));  // Consistency
    }

    @Test
    void testToString() {
        VCClaims credentialSubject = new VCClaims();

        VCData vcData = new VCData(credentialSubject);

        String expectedToString = "VCData(credentialSubject=" + credentialSubject + ")";

        assertEquals(expectedToString, vcData.toString());
    }
}

