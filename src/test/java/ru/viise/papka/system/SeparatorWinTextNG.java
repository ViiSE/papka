package ru.viise.papka.system;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SeparatorWinTextNG {

    private Separator separator;

    @BeforeClass
    public void beforeClass() {
        separator = new SeparatorWin();
    }

    @Test
    public void pure() {
        String pureSeparator = separator.pure();
        assertEquals(pureSeparator, "\\");
    }

    @Test
    public void charS() {
        char charSeparator = separator.charS();
        assertEquals(charSeparator, '\\');
    }

    @Test
    public void regex() {
        String regexSeparator = separator.regex();
        assertEquals(regexSeparator, "\\\\");
    }

    @Test
    public void mirror() {
        Separator unixSeparator = separator.mirror();
        assertNotEquals(separator, unixSeparator);
        assertTrue(unixSeparator instanceof SeparatorUnix);
    }
}
