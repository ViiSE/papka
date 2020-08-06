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

import com.github.viise.papka.system.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.testng.Assert.assertEquals;

public class PreparedFoldersMapFilesRawTestNG {

    @Test
    public void ctor_withFiles() {
        new PreparedFoldersMapFilesRaw(new ArrayList<>());
    }

    @Test
    public void preparation() {
        OsRepository osRepo = new OsRepo();
        Os os = osRepo.instance();
        Separator separator = os.separator();

        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        String name = exDir.name() + "papkaExFolder";

        List<File> files = actualFile(name, separator);

        PreparedFolders<Map<String, List<File>>> preparedFolders = new PreparedFoldersMapFilesRaw(
                os.foldersFile(),
                separator,
                files);

        Map<String, List<File>> actual = new TreeMap<>();
        actual.put("/", new ArrayList<>());
        actual.put(name + separator.pure(), actualFile(name, separator));

        Map<String, List<File>> expected = preparedFolders.preparation();

        assertEquals(expected, actual);
    }

    private List<File> actualFile(String name, Separator separator) {
        List<File> files = new ArrayList<>();
        files.add(new File(name + separator.pure() + "anotherPapkaExFile.txt"));
        files.add(new File(name + separator.pure() + "papkaExFile.txt"));

        return files;
    }
}
