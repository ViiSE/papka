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

package ru.viise.papka.search;

import org.testng.annotations.Test;
import ru.viise.papka.entity.Folder;
import ru.viise.papka.entity.FolderPure;
import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.system.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SearchFolderInSystemTestNG {

    @Test
    public void answer() throws NotFoundException {
        OsRepository osRepo = new OsRepo();
        Separator separator = osRepo.instance().separator();

        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        Folder<File> actual = actualFolder(exDir.name() + "papkaExFolder", separator);

        String name = folderNameWithoutFolderSlash(exDir.name());

        Search<Folder<File>, String> search = new SearchFolderInSystem(name);
        Folder<File> expected = search.answer("^.*papkaExFolder.*$");

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        OsRepository osRepo = new OsRepo();
        Separator separator = osRepo.instance().separator();

        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        String name = folderNameWithoutFolderSlash(exDir.name());
        Search<List<File>, String> search = new SearchFilesByFolderNameInSystem(name);

        search.answer("folderIsNotExist");
    }

    private List<File> actualFile(String name, Separator separator) {
        List<File> files = new ArrayList<>();
        files.add(new File(name + separator.pure() + "anotherPapkaExFile.txt"));
        files.add(new File(name + separator.pure() + "papkaExFile"));
        files.add(new File(name + separator.pure() + "papkaExFile.pdf"));
        files.add(new File(name + separator.pure() + "papkaExFile.txt"));
        files.add(new File(name + separator.pure() + "papkaMusic.mp3"));

        return files;
    }

    private Folder<File> actualFolder(String name, Separator separator) {
        return new FolderPure<>(
                name + separator.pure(),
                actualFile(name, separator));
    }

    private String folderNameWithoutFolderSlash(String name) {
        return name.substring(0, name.length() - 1);
    }
}
