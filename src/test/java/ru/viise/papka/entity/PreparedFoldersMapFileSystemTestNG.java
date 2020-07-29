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

package ru.viise.papka.entity;

import org.testng.annotations.Test;
import ru.viise.papka.system.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.testng.Assert.assertEquals;

public class PreparedFoldersMapFileSystemTestNG {
    
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

        PreparedFolders<Map<String, Folder<File>>> preparedFolders = new PreparedFoldersMapFileSystem(
                new PreparedFoldersMapFilesRaw(
                        os.foldersFile(),
                        separator,
                        files)
        );

        Map<String, Folder<File>> actual = new TreeMap<>();
        Folder<File> folderRoot = new FolderPure<>("/");
        Folder<File> folderEx = new FolderPure<>(
                name + separator.pure(),
                actualFile(name, separator)
        );

        actual.put("/", folderRoot);
        actual.put(name + separator.pure(), folderEx);

        Map<String, Folder<File>> expected = preparedFolders.preparation();

        assertEquals(expected, actual);
    }

    private List<File> actualFile(String name, Separator separator) {
        List<File> files = new ArrayList<>();
        files.add(new File(name + separator.pure() + "anotherPapkaExFile.txt"));
        files.add(new File(name + separator.pure() + "papkaExFile.txt"));

        return files;
    }
}
