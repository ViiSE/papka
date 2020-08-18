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

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@PrepareForTest(System.class)
public class NameSystemDriveWinTestNG extends PowerMockTestCase {

    // all tests   - 10
    // basic tests - 7
    // mock tests  - 3
    private final int basicTestsCount = 7;
    private boolean mockTrigger = false;
    private final String mockSystemDrive = "C:";
    private final String attrName = "SystemDrive";

    private boolean isWindows;
    private Name sysDriveName;

    @BeforeClass
    public void beforeClass() {
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        sysDriveName = new NameSystemDriveWin();
    }

    @Test(priority = 1)
    public void shortName_notWindows() {
        if(!isWindows) {
            String sysDrive = sysDriveName.shortName();
            assertEquals(sysDrive, mockSystemDrive);
        }
    }

    @Test(priority = 2)
    public void fullName_notWindows() {
        if(!isWindows) {
            String sysDrive = sysDriveName.fullName();
            assertEquals(sysDrive, mockSystemDrive);
        }
    }

    @Test(description = "Only on windows", priority = 3)
    public void fullName_windows() {
        if(isWindows) {
            String sysDrive = sysDriveName.fullName();
            assertEquals(sysDrive, System.getenv(attrName));
        }
    }

    @Test(description = "Only on windows", priority = 4)
    public void shortName_windows() {
        if(isWindows) {
            String sysDrive = sysDriveName.shortName();
            assertEquals(sysDrive, System.getenv(attrName));
        }
    }

    @Test(priority = 5)
    public void eq() {
        if(isWindows) {
            Name expected = new NamePure(System.getenv(attrName));
            assertEquals(expected, sysDriveName);
        } else {
            Name expected = new NamePure(mockSystemDrive);
            assertEquals(expected, sysDriveName);
        }
    }

    @Test(priority = 6)
    public void eq_wrongType() {
        assertNotEquals(mockSystemDrive, sysDriveName);
    }

    @Test(priority = 7)
    public void eq_no() {
        Name expected = new NamePure("C:\\music");
        assertNotEquals(expected, sysDriveName);
    }

    @Test(description = "Windows mock", priority = 8)
    public void fullName_windowsMock() {
        String sysDrive = sysDriveName.fullName();
        assertEquals(sysDrive, mockSystemDrive);
    }

    @Test(description = "Windows mock", priority = 9)
    public void shortName_windowsMock() {
        String sysDrive = sysDriveName.shortName();
        assertEquals(sysDrive, mockSystemDrive);
    }

    @Test(priority = 10)
    public void eqMock() {
        Name expected = new NamePure(mockSystemDrive);
        assertEquals(expected, sysDriveName);
    }

    @AfterMethod
    public void afterTest(ITestContext ctx) {
        if(ctx.getPassedTests().size() == basicTestsCount) {
            if(!mockTrigger) {
                PowerMockito.mockStatic(System.class);
                PowerMockito.when(System.getenv(attrName)).thenReturn(mockSystemDrive);
                PowerMockito.verifyStatic(System.class);

                mockTrigger = true;
            }
        }
    }
}
