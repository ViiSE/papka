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

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FoldersFileNameTestNG {

    private FoldersFile<String, String> foldersFile;

    @BeforeClass
    public void beforeClass() {
        foldersFile = new FoldersFileName();
    }

    @Test
    public void folders_withRoot() {
        String rawFolder = "/music/classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_withoutRoot() {
        String rawFolder = "music/classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_doubleSlash() {
        String rawFolder = "/music//classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_withoutExt() {
        String rawFolder = "/music/classic/chopin";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_withoutFile() {
        String rawFolder = "/music/classic/chopin/";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
            add("chopin");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }
}
