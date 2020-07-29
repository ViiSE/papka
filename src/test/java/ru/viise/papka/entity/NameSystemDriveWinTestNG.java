package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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
}