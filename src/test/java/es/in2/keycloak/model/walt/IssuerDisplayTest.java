package es.in2.keycloak.model.walt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IssuerDisplayTest {

    @Test
    void testIssuerDisplay() {
        IssuerDisplay issuerDisplay = new IssuerDisplay();
        Assertions.assertNotNull(issuerDisplay);
    }
}
