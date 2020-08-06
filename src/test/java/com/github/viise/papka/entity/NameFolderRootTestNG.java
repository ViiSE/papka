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

public class NameFolderRootTestNG {

    private final String rootName = "/";
    private Name name;

    @BeforeClass
    public void beforeClass() {
        name = new NameFolderRoot();
    }

    @Test
    public void shortName() {
        assertEquals(name.shortName(), rootName);
    }

    @Test
    public void fullName() {
        assertEquals(name.fullName(), rootName);
    }

    @Test
    public void eq() {
        Name expected = new NamePure("/");
        assertEquals(expected, name);
    }

    @Test
    public void eq_wrongType() {
        String expected = "/";
        assertNotEquals(expected, name);
    }

    @Test
    public void eq_no() {
        Name expected = new NamePure("/music");
        assertNotEquals(expected, name);
    }
}
