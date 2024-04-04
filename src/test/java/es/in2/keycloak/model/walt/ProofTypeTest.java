package es.in2.keycloak.model.walt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProofTypeTest {

    @Test
    void testEnumValues() {
        ProofType ldProof = ProofType.LD_PROOF;
        assertNotNull(ldProof);
        assertEquals("LD_PROOF", ldProof.getValue());

        ProofType jwt = ProofType.JWT;
        assertNotNull(jwt);
        assertEquals("JWT", jwt.getValue());
    }
}
