package es.in2.keycloak.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
