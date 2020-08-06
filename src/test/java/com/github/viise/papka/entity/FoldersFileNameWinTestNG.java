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

import com.github.viise.papka.system.Separator;
import org.testng.annotations.Test;
import com.github.viise.papka.system.SeparatorUnix;
import com.github.viise.papka.system.SeparatorWin;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FoldersFileNameWinTestNG {

    @Test
    public void folders_unixWithoutRoot() {
        Separator separator = new SeparatorUnix();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "C:/music/classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_unixWithRoot() {
        Separator separator = new SeparatorUnix();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "/C:/music/classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_winWithoutRoot() {
        Separator separator = new SeparatorWin();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "C:\\music\\classic\\chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_winWithRoot() {
        Separator separator = new SeparatorWin();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "/\\C:\\music\\classic\\chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_winWithoutSystemDrive() {
        Separator separator = new SeparatorWin();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "music\\classic\\chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }
}
