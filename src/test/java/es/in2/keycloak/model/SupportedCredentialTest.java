package es.in2.keycloak.model;

import org.fiware.keycloak.oidcvc.model.FormatVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SupportedCredentialTest {

    @Test
    void testNoArgsConstructor() {
        SupportedCredential supportedCredential = new SupportedCredential();

        assertNull(supportedCredential.getType());
        assertNull(supportedCredential.getFormat());
    }

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

    @Test
    void testEqualsAndHashCode() {
        String type1 = "type1";
        FormatVO format1 = FormatVO.JWT_VC_JSON;
        String type2 = "type2";
        FormatVO format2 = FormatVO.JWT_VC_JSON_LD;

        SupportedCredential supportedCredential1 = new SupportedCredential(type1, format1);
        SupportedCredential supportedCredential2 = new SupportedCredential(type1, format1);
        SupportedCredential supportedCredential3 = new SupportedCredential(type2, format2);

        assertEquals(supportedCredential1, supportedCredential2);
        assertEquals(supportedCredential1.hashCode(), supportedCredential2.hashCode());

        assertEquals(supportedCredential1, supportedCredential1);  // Reflexivity
        assertEquals(supportedCredential1.equals(supportedCredential2), supportedCredential2.equals(supportedCredential1));  // Symmetry
        assertEquals(supportedCredential1.equals(supportedCredential2) && supportedCredential2.equals(supportedCredential3), supportedCredential1.equals(supportedCredential3));  // Transitivity
        assertEquals(supportedCredential1.equals(supportedCredential2), supportedCredential1.equals(supportedCredential2));  // Consistency
    }

    @Test
    void testToString() {
        String type = "type";
        FormatVO format = FormatVO.JWT_VC_JSON;

        SupportedCredential supportedCredential = new SupportedCredential(type, format);

        String expectedToString = "SupportedCredential(type=" + type + ", format=" + format + ")";

        assertEquals(expectedToString, supportedCredential.toString());
    }
}
