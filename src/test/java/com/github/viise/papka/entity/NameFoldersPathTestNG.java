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

import com.github.viise.papka.system.SeparatorWin;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class NameFoldersPathTestNG {

    private List<String> foldersName;

    @BeforeClass
    public void beforeClass() {
        foldersName = new ArrayList<>();
        foldersName.add("/");
        foldersName.add("C:");
        foldersName.add("files");
        foldersName.add("music");
        foldersName.add("opus");
    }

    @Test
    public void fullName_unix() {
        Name fPathName = new NameFoldersPath(foldersName);
        String fullName = fPathName.fullName();

        assertEquals(fullName, "/C:/files/music/opus/");
    }

    @Test
    public void shortName_unix() {
        Name fPathName = new NameFoldersPath(foldersName);
        String shortName = fPathName.shortName();

        assertEquals(shortName, "opus");
    }

    @Test
    public void fullName_win() {
        Name fPathName = new NameFoldersPath(foldersName, new SeparatorWin());
        String fullName = fPathName.fullName();

        assertEquals(fullName, "/\\C:\\files\\music\\opus\\");
    }

    @Test
    public void shortName_win() {
        Name fPathName = new NameFoldersPath(foldersName, new SeparatorWin());
        String shortName = fPathName.shortName();

        assertEquals(shortName, "opus");
    }

    @Test
    public void shortName_twiceForCheckInit() {
        Name fPathName = new NameFoldersPath(foldersName, new SeparatorWin());
        String shortName = fPathName.shortName();
        fPathName.shortName();

        assertEquals(shortName, "opus");
    }

    @Test
    public void eq() {
        Name actual = new NameFoldersPath(foldersName);
        Name expected = new NameFoldersPath(new ArrayList<String>() {{
            add("/");
            add("C:");
            add("files");
            add("music");
            add("opus");
        }});

        assertEquals(expected, actual);
    }

    @Test
    public void eq_wrongType() {
        Name actual = new NameFoldersPath(foldersName);
        String expected = "/";
        assertNotEquals(expected, actual);
    }

    @Test
    public void eq_no() {
        Name actual = new NameFoldersPath(foldersName);
        Name expected = new NamePure("/music");
        assertNotEquals(expected, actual);
    }
}
