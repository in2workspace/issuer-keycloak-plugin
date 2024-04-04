package es.in2.keycloak.model;

import org.fiware.keycloak.oidcvc.model.ProofTypeVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestProofTest {

    @Test
    void testAllArgsConstructor() {
        ProofTypeVO proofType = ProofTypeVO.LD_PROOF;
        RequestProof requestProof = new RequestProof(proofType, "testJwt");

        assertEquals("ld_proof", requestProof.getProofType().toString());
        assertEquals("testJwt", requestProof.getJwt());
    }

    @Test
    void testGetterSetter() {
        RequestProof requestProof = new RequestProof();
        ProofTypeVO proofType = ProofTypeVO.JWT;
        requestProof.setProofType(proofType);
        requestProof.setJwt("newJwt");

        assertEquals("jwt", requestProof.getProofType().toString());
        assertEquals("newJwt", requestProof.getJwt());
    }
}

