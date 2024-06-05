package es.in2.keycloak.model;

import org.fiware.keycloak.oidcvc.model.ProofTypeVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestProofTest {

    @Test
    void testNoArgsConstructor() {
        RequestProof requestProof = new RequestProof();

        assertNull(requestProof.getProofType());
        assertNull(requestProof.getJwt());
    }

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

    @Test
    void testEqualsAndHashCode() {
        ProofTypeVO proofType1 = ProofTypeVO.JWT;
        ProofTypeVO proofType2 = ProofTypeVO.LD_PROOF;
        String jwt1 = "exampleJWT1";
        String jwt2 = "exampleJWT2";

        RequestProof requestProof1 = new RequestProof(proofType1, jwt1);
        RequestProof requestProof2 = new RequestProof(proofType1, jwt1);
        RequestProof requestProof3 = new RequestProof(proofType2, jwt1);
        RequestProof requestProof4 = new RequestProof(proofType1, jwt2);

        assertEquals(requestProof1, requestProof2);
        assertEquals(requestProof1.hashCode(), requestProof2.hashCode());

        assertNotEquals(requestProof1, requestProof3);
        assertNotEquals(requestProof1.hashCode(), requestProof3.hashCode());

        assertNotEquals(requestProof1, requestProof4);
        assertNotEquals(requestProof1.hashCode(), requestProof4.hashCode());
    }

    @Test
    void testToString() {
        ProofTypeVO proofType = ProofTypeVO.JWT;
        String jwt = "exampleJWT";

        RequestProof requestProof = new RequestProof(proofType, jwt);

        String expectedToString = "RequestProof(proofType=" + proofType + ", jwt=" + jwt + ")";

        assertEquals(expectedToString, requestProof.toString());
    }
}

