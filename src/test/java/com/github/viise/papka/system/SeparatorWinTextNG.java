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

package com.github.viise.papka.system;

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
