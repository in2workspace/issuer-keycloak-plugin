package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testNoArgsConstructor() {
        VCClaims vcClaims = new VCClaims();

        // Test fields initialized by no-args constructor
        assertNull(vcClaims.getFirstName());
        assertNull(vcClaims.getFamilyName());
        assertNull(vcClaims.getEmail());
        assertNull(vcClaims.getRoles());
        assertNull(vcClaims.getAdditionalClaims());
    }

    @Test
    void testJsonAnyGetter() {
        VCClaims vcClaims = new VCClaims();

        Map<String, String> additionalClaims = new HashMap<>();
        additionalClaims.put("key1", "value1");
        additionalClaims.put("key2", "value2");

        vcClaims.setAdditionalClaims(additionalClaims);

        // Test JsonAnyGetter method
        assertEquals(additionalClaims, vcClaims.getAdditionalClaims());
    }

    @Test
    void testEqualsAndHashCode() {
        String firstName1 = "John";
        String familyName1 = "Doe";
        String email1 = "john.doe@example.com";
        Set<Role> roles1 = new HashSet<>();
        roles1.add(new Role());
        Map<String, String> additionalClaims1 = new HashMap<>();
        additionalClaims1.put("key1", "value1");
        additionalClaims1.put("key2", "value2");

        String firstName2 = "Jane";
        String familyName2 = "Doe";
        String email2 = "jane.doe@example.com";
        Set<Role> roles2 = new HashSet<>();
        roles2.add(new Role());
        Map<String, String> additionalClaims2 = new HashMap<>();
        additionalClaims2.put("key1", "value1");
        additionalClaims2.put("key2", "value2");

        VCClaims vcClaims1 = new VCClaims(firstName1, familyName1, email1, roles1, additionalClaims1);
        VCClaims vcClaims2 = new VCClaims(firstName1, familyName1, email1, roles1, additionalClaims1);
        VCClaims vcClaims3 = new VCClaims(firstName2, familyName2, email2, roles2, additionalClaims2);

        assertEquals(vcClaims1, vcClaims2);
        assertEquals(vcClaims1.hashCode(), vcClaims2.hashCode());

        assertEquals(vcClaims1, vcClaims1);  // Reflexivity
        assertEquals(vcClaims1.equals(vcClaims2), vcClaims2.equals(vcClaims1));  // Symmetry
        assertEquals(vcClaims1.equals(vcClaims2) && vcClaims2.equals(vcClaims3), vcClaims1.equals(vcClaims3));  // Transitivity
        assertEquals(vcClaims1.equals(vcClaims2), vcClaims1.equals(vcClaims2));  // Consistency
    }

    @Test
    void testToString() {
        String firstName = "John";
        String familyName = "Doe";
        String email = "john.doe@example.com";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role());
        Map<String, String> additionalClaims = new HashMap<>();
        additionalClaims.put("key1", "value1");
        additionalClaims.put("key2", "value2");

        VCClaims vcClaims = new VCClaims(firstName, familyName, email, roles, additionalClaims);

        String expectedToString = "VCClaims(firstName=" + firstName + ", familyName=" + familyName +
                ", email=" + email + ", roles=" + roles + ", additionalClaims=" + additionalClaims + ")";

        assertEquals(expectedToString, vcClaims.toString());
    }
}
