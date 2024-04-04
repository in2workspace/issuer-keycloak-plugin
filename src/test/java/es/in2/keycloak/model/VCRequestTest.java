package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VCRequestTest {

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
}
