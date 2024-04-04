package es.in2.keycloak.model.walt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

 class FormatObjectTest {

    @Test
    void testAllArgsConstructor() {
        List<String> types = new ArrayList<>();
        types.add("type1");
        types.add("type2");

        FormatObject formatObject = new FormatObject(types);

        assertEquals(types, formatObject.getTypes());
    }
    @Test
    void testGettersAndSetters() {
        List<String> types = new ArrayList<>();
        types.add("type1");
        types.add("type2");

        FormatObject formatObject = new FormatObject();
        formatObject.setTypes(types);

        assertEquals(types, formatObject.getTypes());
    }

}
