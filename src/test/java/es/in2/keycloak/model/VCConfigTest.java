package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VCConfigTest {

    @Test
    void testNoArgsConstructor() {
        VCConfig vcConfig = new VCConfig();

        // Test fields initialized by no-args constructor
        assertNull(vcConfig.getIssuerDid());
        assertNull(vcConfig.getSubjectDid());
        assertNull(vcConfig.getProofType());
        assertNull(vcConfig.getExpirationDate());
    }

    @Test
    void testAllArgsConstructor() {
        VCConfig vcConfig = new VCConfig("issuerDid", "subjectDid", "proofType", "expirationDate");

        assertEquals("issuerDid", vcConfig.getIssuerDid());
        assertEquals("subjectDid", vcConfig.getSubjectDid());
        assertEquals("proofType", vcConfig.getProofType());
        assertEquals("expirationDate", vcConfig.getExpirationDate());
    }

    @Test
    void testBuilder() {
        VCConfig vcConfig = VCConfig.builder()
                .issuerDid("issuerDid")
                .subjectDid("subjectDid")
                .proofType("proofType")
                .expirationDate("expirationDate")
                .build();

        assertEquals("issuerDid", vcConfig.getIssuerDid());
        assertEquals("subjectDid", vcConfig.getSubjectDid());
        assertEquals("proofType", vcConfig.getProofType());
        assertEquals("expirationDate", vcConfig.getExpirationDate());
    }

    @Test
    void testGetterSetter() {
        VCConfig vcConfig = new VCConfig();

        vcConfig.setIssuerDid("issuerDid");
        vcConfig.setSubjectDid("subjectDid");
        vcConfig.setProofType("proofType");
        vcConfig.setExpirationDate("expirationDate");

        assertEquals("issuerDid", vcConfig.getIssuerDid());
        assertEquals("subjectDid", vcConfig.getSubjectDid());
        assertEquals("proofType", vcConfig.getProofType());
        assertEquals("expirationDate", vcConfig.getExpirationDate());
    }

    @Test
    void testEqualsAndHashCode() {
        String issuerDid1 = "issuerDid1";
        String subjectDid1 = "subjectDid1";
        String proofType1 = "proofType1";
        String expirationDate1 = "expirationDate1";

        String issuerDid2 = "issuerDid2";
        String subjectDid2 = "subjectDid2";
        String proofType2 = "proofType2";
        String expirationDate2 = "expirationDate2";

        VCConfig vcConfig1 = new VCConfig(issuerDid1, subjectDid1, proofType1, expirationDate1);
        VCConfig vcConfig2 = new VCConfig(issuerDid1, subjectDid1, proofType1, expirationDate1);
        VCConfig vcConfig3 = new VCConfig(issuerDid2, subjectDid2, proofType2, expirationDate2);

        assertEquals(vcConfig1, vcConfig2);
        assertEquals(vcConfig1.hashCode(), vcConfig2.hashCode());

        assertEquals(vcConfig1, vcConfig1);  // Reflexivity
        assertEquals(vcConfig1.equals(vcConfig2), vcConfig2.equals(vcConfig1));  // Symmetry
        assertEquals(vcConfig1.equals(vcConfig2) && vcConfig2.equals(vcConfig3), vcConfig1.equals(vcConfig3));  // Transitivity
        assertEquals(vcConfig1.equals(vcConfig2), vcConfig1.equals(vcConfig2));  // Consistency
    }

    @Test
    void testToString() {
        String issuerDid = "issuerDid";
        String subjectDid = "subjectDid";
        String proofType = "proofType";
        String expirationDate = "expirationDate";

        VCConfig vcConfig = new VCConfig(issuerDid, subjectDid, proofType, expirationDate);

        String expectedToString = "VCConfig(issuerDid=" + issuerDid + ", subjectDid=" + subjectDid +
                ", proofType=" + proofType + ", expirationDate=" + expirationDate + ")";

        assertEquals(expectedToString, vcConfig.toString());
    }
}
