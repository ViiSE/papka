package ru.viise.papka.system;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SeparatorUnixTextNG {

    private Separator separator;

    @BeforeClass
    public void beforeClass() {
        separator = new SeparatorUnix();
    }

    @Test
    public void pure() {
        String pureSeparator = separator.pure();
        assertEquals(pureSeparator, "/");
    }

    @Test
    public void charS() {
        char charSeparator = separator.charS();
        assertEquals(charSeparator, '/');
    }

    @Test
    public void regex() {
        String regexSeparator = separator.regex();
        assertEquals(regexSeparator, "/");
    }

    @Test
    public void mirror() {
        Separator winSeparator = separator.mirror();
        assertNotEquals(separator, winSeparator);
        assertTrue(winSeparator instanceof SeparatorWin);
    }
}
