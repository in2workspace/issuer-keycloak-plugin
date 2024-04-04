package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VCConfigTest {

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
}
