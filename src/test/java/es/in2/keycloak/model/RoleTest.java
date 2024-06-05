package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoleTest {

    @Test
    void testAllArgsConstructor() {
        Set<String> names = new HashSet<>();
        names.add("admin");
        names.add("user");

        Role role = new Role(names, "target");

        assertEquals(names, role.getNames());
        assertEquals("target", role.getTarget());
    }

    @Test
    void testGetterSetter() {
        Role role = new Role();
        Set<String> names = new HashSet<>();
        names.add("manager");
        names.add("employee");

        role.setNames(names);
        role.setTarget("newTarget");

        assertEquals(names, role.getNames());
        assertEquals("newTarget", role.getTarget());
    }

    @Test
    void testNoArgsConstructor() {
        Role role = new Role();

        assertNull(role.getNames());
        assertNull(role.getTarget());
    }

    @Test
    void testEqualsAndHashCode() {
        Set<String> names1 = new HashSet<>();
        names1.add("role1");
        names1.add("role2");
        String target1 = "target1";

        Set<String> names2 = new HashSet<>();
        names2.add("role1");
        names2.add("role2");
        String target2 = "target1";

        Role role1 = new Role(names1, target1);
        Role role2 = new Role(names1, target1);
        Role role3 = new Role(names2, target2);

        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());

        assertEquals(role1, role1);  // Reflexivity
        assertEquals(role1.equals(role2), role2.equals(role1));  // Symmetry
        assertEquals(role1.equals(role2) && role2.equals(role3), role1.equals(role3));  // Transitivity
        assertEquals(role1.equals(role2), role1.equals(role2));  // Consistency
    }

    @Test
    void testToString() {
        Set<String> names = new HashSet<>();
        names.add("role1");
        names.add("role2");
        String target = "target";

        Role role = new Role(names, target);

        String expectedToString = "Role(names=" + names + ", target=" + target + ")";

        assertEquals(expectedToString, role.toString());
    }
}
