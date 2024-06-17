package es.in2.keycloak.model.walt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.fiware.keycloak.oidcvc.model.DisplayObjectVO;

import java.util.ArrayList;
import java.util.List;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IssuerDisplayTest {

    @Test
    void testIssuerDisplay() {
        IssuerDisplay issuerDisplay = new IssuerDisplay();
        Assertions.assertNotNull(issuerDisplay);
    }

    @Test
    void testNoArgsConstructor() {
        IssuerDisplay issuerDisplay = new IssuerDisplay();

        assertNotNull(issuerDisplay.getDisplay());
        assertEquals(0, issuerDisplay.getDisplay().size());
    }

    @Test
    void testAllArgsConstructor() {
        List<DisplayObjectVO> display = new ArrayList<>();
        display.add(new DisplayObjectVO());

        IssuerDisplay issuerDisplay = new IssuerDisplay(display);

        assertNotNull(issuerDisplay.getDisplay());
        assertEquals(1, issuerDisplay.getDisplay().size());
    }

    @Test
    void testSetterAndGetter() {
        IssuerDisplay issuerDisplay = new IssuerDisplay();

        List<DisplayObjectVO> display = new ArrayList<>();
        display.add(new DisplayObjectVO());

        issuerDisplay.setDisplay(display);

        assertNotNull(issuerDisplay.getDisplay());
        assertEquals(1, issuerDisplay.getDisplay().size());
    }

    @Test
    void testEqualsAndHashCode() {
        List<DisplayObjectVO> display1 = new ArrayList<>();
        display1.add(new DisplayObjectVO());

        List<DisplayObjectVO> display2 = new ArrayList<>();
        display2.add(new DisplayObjectVO());

        IssuerDisplay issuerDisplay1 = new IssuerDisplay(display1);
        IssuerDisplay issuerDisplay2 = new IssuerDisplay(display2);

        assertEquals(issuerDisplay1, issuerDisplay2);
        assertEquals(issuerDisplay1.hashCode(), issuerDisplay2.hashCode());
    }

    @Test
    void testToString() {
        List<DisplayObjectVO> display = new ArrayList<>();
        display.add(new DisplayObjectVO());
        display.add(new DisplayObjectVO());

        IssuerDisplay issuerDisplay = new IssuerDisplay(display);

        String expectedToString = "IssuerDisplay(display=[class DisplayObjectVO {\n" +
                "    name: null\n" +
                "    locale: null\n" +
                "}, class DisplayObjectVO {\n" +
                "    name: null\n" +
                "    locale: null\n" +
                "}])";

        assertEquals(expectedToString, issuerDisplay.toString());
    }
}
