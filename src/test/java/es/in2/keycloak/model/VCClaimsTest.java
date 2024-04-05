package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VCClaimsTest {

    @Test
    void testAllArgsConstructor() {
        Map<String, String> additionalClaims = new HashMap<>();
        additionalClaims.put("key1", "value1");
        additionalClaims.put("key2", "value2");

        VCClaims vcClaims = new VCClaims("John", "Doe", "john@example.com", Set.of(new Role(Set.of("admin"), "target")), additionalClaims);

        assertEquals("John", vcClaims.getFirstName());
        assertEquals("Doe", vcClaims.getFamilyName());
        assertEquals("john@example.com", vcClaims.getEmail());
        assertNotNull(vcClaims.getRoles());
        assertEquals(1, vcClaims.getRoles().size());
        assertEquals("admin", vcClaims.getRoles().iterator().next().getNames().iterator().next());
        assertEquals("target", vcClaims.getRoles().iterator().next().getTarget());
        assertNotNull(vcClaims.getAdditionalClaims());
        assertEquals(2, vcClaims.getAdditionalClaims().size());
        assertEquals("value1", vcClaims.getAdditionalClaims().get("key1"));
        assertEquals("value2", vcClaims.getAdditionalClaims().get("key2"));
    }

    @Test
    void testBuilder() {
        VCClaims vcClaims = VCClaims.builder()
                .firstName("John")
                .familyName("Doe")
                .email("john@example.com")
                .roles(Set.of(new Role(Set.of("admin"), "target")))
                .additionalClaims(Map.of("key1", "value1", "key2", "value2"))
                .build();

        assertEquals("John", vcClaims.getFirstName());
        assertEquals("Doe", vcClaims.getFamilyName());
        assertEquals("john@example.com", vcClaims.getEmail());
        assertNotNull(vcClaims.getRoles());
        assertEquals(1, vcClaims.getRoles().size());
        assertEquals("admin", vcClaims.getRoles().iterator().next().getNames().iterator().next());
        assertEquals("target", vcClaims.getRoles().iterator().next().getTarget());
        assertNotNull(vcClaims.getAdditionalClaims());
        assertEquals(2, vcClaims.getAdditionalClaims().size());
        assertEquals("value1", vcClaims.getAdditionalClaims().get("key1"));
        assertEquals("value2", vcClaims.getAdditionalClaims().get("key2"));
    }

    @Test
    void testGetterSetter() {
        VCClaims vcClaims = new VCClaims("","","",null,null);

        vcClaims.setFirstName("John");
        vcClaims.setFamilyName("Doe");
        vcClaims.setEmail("john@example.com");
        vcClaims.setRoles(Set.of(new Role(Set.of("admin"), "target")));
        vcClaims.setAdditionalClaims(Map.of("key1", "value1", "key2", "value2"));

        assertEquals("John", vcClaims.getFirstName());
        assertEquals("Doe", vcClaims.getFamilyName());
        assertEquals("john@example.com", vcClaims.getEmail());
        assertNotNull(vcClaims.getRoles());
        assertEquals(1, vcClaims.getRoles().size());
        assertEquals("admin", vcClaims.getRoles().iterator().next().getNames().iterator().next());
        assertEquals("target", vcClaims.getRoles().iterator().next().getTarget());
        assertNotNull(vcClaims.getAdditionalClaims());
        assertEquals(2, vcClaims.getAdditionalClaims().size());
        assertEquals("value1", vcClaims.getAdditionalClaims().get("key1"));
        assertEquals("value2", vcClaims.getAdditionalClaims().get("key2"));
    }
}
