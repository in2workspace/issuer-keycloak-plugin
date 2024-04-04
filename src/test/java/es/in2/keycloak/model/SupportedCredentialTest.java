package es.in2.keycloak.model;

import org.fiware.keycloak.oidcvc.model.FormatVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupportedCredentialTest {

    @Test
    void testAllArgsConstructor() {
        FormatVO formatVO = FormatVO.JWT_VC_JSON;
        SupportedCredential supportedCredential = new SupportedCredential("typeValue", formatVO);

        assertEquals("typeValue", supportedCredential.getType());
        assertEquals(formatVO, supportedCredential.getFormat());
    }

    @Test
    void testGetterSetter() {
        SupportedCredential supportedCredential = new SupportedCredential();
        FormatVO formatVO = FormatVO.JWT_VC_JSON;

        supportedCredential.setType("newType");
        supportedCredential.setFormat(formatVO);

        assertEquals("newType", supportedCredential.getType());
        assertEquals(formatVO, supportedCredential.getFormat());
    }
}
