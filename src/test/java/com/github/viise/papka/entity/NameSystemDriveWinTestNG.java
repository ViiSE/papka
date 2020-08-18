/*
 * Copyright 2020 ViiSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class NameSystemDriveWinTestNG {

    // all tests   - 10
    // basic tests - 7
    // mock tests  - 3
    private final int basicTestsCount = 7;
    private boolean mockTrigger = false;

    private boolean isWindows;
    private Name sysDriveName;

    @BeforeClass
    public void beforeClass() {
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        sysDriveName = new NameSystemDriveWin();
    }

    @Test
    public void ctor_withSystemDriveName() {
        new NameSystemDriveWin("C:");
    }

    @Test
    public void shortName_notWindows() {
        if(!isWindows) {
            String sysDrive = sysDriveName.shortName();
            assertEquals(sysDrive, "C:");
        }
    }

    @Test
    public void fullName_notWindows() {
        if(!isWindows) {
            String sysDrive = sysDriveName.fullName();
            assertEquals(sysDrive, "C:");
        }
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
        assertNotEquals("C:", sysDriveName);
    }

    @Test
    public void eq_no() {
        Name expected = new NamePure("C:\\music");
        assertNotEquals(expected, sysDriveName);
    }

    @Test
    public void fullName_customSystemDrive() {
        NameSystemDriveWin customSysDrive = new NameSystemDriveWin("C:");

        String sysDrive = customSysDrive.fullName();
        assertEquals(sysDrive, "C:");
    }

    @Test(description = "Windows mock")
    public void shortName_windowsMock() {
        NameSystemDriveWin customSysDrive = new NameSystemDriveWin("C:");

        String sysDrive = customSysDrive.shortName();
        assertEquals(sysDrive, "C:");
    }

    @Test
    public void eqMock() {
        NameSystemDriveWin customSysDrive = new NameSystemDriveWin("C:");

        Name expected = new NamePure("C:");
        assertEquals(expected, customSysDrive);
    }
}
