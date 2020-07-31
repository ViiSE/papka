package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class NameSystemDriveWinTestNG {

    private boolean isWindows;

    private Name sysDriveName;

    @BeforeClass
    public void beforeClass() {
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        sysDriveName = new NameSystemDriveWin();
    }

    @Test
    public void shortName_notWindows() {
        String sysDrive = sysDriveName.shortName();
        assertEquals(sysDrive, "C:");
    }

    @Test
    public void fullName_notWindows() {
        String sysDrive = sysDriveName.fullName();
        assertEquals(sysDrive, "C:");
    }

    @Test(description = "Only on windows")
    public void fullName_windows() {
        if(isWindows) {
            String sysDrive = sysDriveName.fullName();
            assertEquals(sysDrive, System.getenv("SystemDrive"));
        }
    }

    @Test(description = "Only on windows")
    public void shortName_windows() {
        if(isWindows) {
            String sysDrive = sysDriveName.shortName();
            assertEquals(sysDrive, System.getenv("SystemDrive"));
        }
    }

    @Test
    public void eq() {
        if(isWindows) {
            Name expected = new NamePure(System.getenv("SystemDrive"));
            assertEquals(expected, sysDriveName);
        } else {
            Name expected = new NamePure("C:");
            assertEquals(expected, sysDriveName);
        }
    }

    @Test
    public void eq_wrongType() {
        String expected = "C:";
        assertNotEquals(expected, sysDriveName);
    }

    @Test
    public void eq_no() {
        Name expected = new NamePure("C:\\music");
        assertNotEquals(expected, sysDriveName);
    }
}
