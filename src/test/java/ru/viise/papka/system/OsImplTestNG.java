package ru.viise.papka.system;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class OsImplTestNG {

    private Os os;

    @BeforeClass
    public void beforeClass() {
        os = new OsImpl();
    }

    @Test
    public void isWindows() {
        boolean isW = os.isWindows();
        assertEquals(isW, System.getProperty("os.name").toLowerCase().contains("windows"));
    }

    @Test
    public void isLinux() {
        boolean isL = os.isLinux();
        assertEquals(isL, System.getProperty("os.name").toLowerCase().contains("linux"));
    }

    @Test
    public void isMac() {
        boolean isM = os.isMac();
        assertEquals(isM, System.getProperty("os.name").toLowerCase().contains("mac"));
    }
}
