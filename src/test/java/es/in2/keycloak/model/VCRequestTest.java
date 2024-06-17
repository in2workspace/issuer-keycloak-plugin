package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VCRequestTest {

    @Test
    void testNoArgsConstructor() {
        VCRequest vcRequest = new VCRequest();

        // Test fields initialized by no-args constructor
        assertNull(vcRequest.getTemplateId());
        assertNull(vcRequest.getConfig());
        assertNull(vcRequest.getCredentialData());
    }

    @Test
    void testAllArgsConstructor() {
        VCConfig vcConfig = new VCConfig();
        vcConfig.setIssuerDid("issuer123");

        VCData vcData = new VCData();

        VCRequest vcRequest = new VCRequest("template123", vcConfig, vcData);

        assertEquals("template123", vcRequest.getTemplateId());
        assertNotNull(vcRequest.getConfig());
        assertEquals("issuer123", vcRequest.getConfig().getIssuerDid());
        assertNotNull(vcRequest.getCredentialData());
    }

    @Test
    void testBuilder() {
        VCConfig vcConfig = new VCConfig();
        vcConfig.setIssuerDid("issuer123");

        VCData vcData = new VCData();

        VCRequest vcRequest = VCRequest.builder()
                .templateId("template123")
                .config(vcConfig)
                .credentialData(vcData)
                .build();

        assertEquals("template123", vcRequest.getTemplateId());
        assertNotNull(vcRequest.getConfig());
        assertEquals("issuer123", vcRequest.getConfig().getIssuerDid());
        assertNotNull(vcRequest.getCredentialData());
    }

    @Test
    void testGetterSetter() {
        VCRequest vcRequest = new VCRequest();

        VCConfig vcConfig = new VCConfig();
        vcConfig.setIssuerDid("issuer123");

        VCData vcData = new VCData();

        vcRequest.setTemplateId("template123");
        vcRequest.setConfig(vcConfig);
        vcRequest.setCredentialData(vcData);

        assertEquals("template123", vcRequest.getTemplateId());
        assertNotNull(vcRequest.getConfig());
        assertEquals("issuer123", vcRequest.getConfig().getIssuerDid());
        assertNotNull(vcRequest.getCredentialData());
    }

    @Test
    void testEqualsAndHashCode() {
        String templateId1 = "templateId1";
        VCConfig config1 = new VCConfig();
        VCData credentialData1 = new VCData();

        String templateId2 = "templateId2";
        VCConfig config2 = new VCConfig();
        VCData credentialData2 = new VCData();

        VCRequest vcRequest1 = new VCRequest(templateId1, config1, credentialData1);
        VCRequest vcRequest2 = new VCRequest(templateId1, config1, credentialData1);
        VCRequest vcRequest3 = new VCRequest(templateId2, config2, credentialData2);

        assertEquals(vcRequest1, vcRequest2);
        assertEquals(vcRequest1.hashCode(), vcRequest2.hashCode());

        assertEquals(vcRequest1, vcRequest1);  // Reflexivity
        assertEquals(vcRequest1.equals(vcRequest2), vcRequest2.equals(vcRequest1));  // Symmetry
        assertEquals(vcRequest1.equals(vcRequest2) && vcRequest2.equals(vcRequest3), vcRequest1.equals(vcRequest3));  // Transitivity
        assertEquals(vcRequest1.equals(vcRequest2), vcRequest1.equals(vcRequest2));  // Consistency
    }

    @Test
    void testToString() {
        String templateId = "templateId";
        VCConfig config = new VCConfig();
        VCData credentialData = new VCData();

        VCRequest vcRequest = new VCRequest(templateId, config, credentialData);

        String expectedToString = "VCRequest(templateId=" + templateId + ", config=" + config +
                ", credentialData=" + credentialData + ")";

        assertEquals(expectedToString, vcRequest.toString());
    }
}
