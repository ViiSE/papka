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

package com.github.viise.papka.search;

import com.github.viise.papka.exception.NotFoundException;
import com.github.viise.papka.system.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SearchFilesInSystemTestNG {

    @Test
    public void answer() throws NotFoundException {
        OsRepository osRepo = new OsRepo();
        Separator separator = osRepo.instance().separator();

        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        String name = exDir.name() + "papkaExFolder";

        List<File> actual = actualFile(name, separator);

        Search<List<File>, String> search = new SearchFilesInSystem(name);
        List<File> expected = search.answer("([^.]*)(" + ".txt" + "$)");
        Collections.sort(expected);

        assertEquals(expected, actual);
    }

    @Test
    public void answer_maxDepthIs1() throws NotFoundException {
        OsRepository osRepo = new OsRepo();
        Separator separator = osRepo.instance().separator();

        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        String name = exDir.name() + "depth";

        List<File> actual = actualFile(name, separator);

        Search<List<File>, String> search = new SearchFilesInSystem(name, 1);
        List<File> expected = search.answer("([^.]*)(" + ".txt" + "$)");
        Collections.sort(expected);

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        OsRepository osRepo = new OsRepo();
        Separator separator = osRepo.instance().separator();

        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        String name = exDir.name() + "papkaExFolder";
        Search<List<File>, String> search = new SearchFilesInSystem(name);

        search.answer("thisFilesIsNotExist.txt");
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_ioException() throws NotFoundException {
        Search<List<File>, String> search = new SearchFilesInSystem("ERROR");
        search.answer("ERROR");
    }

    private List<File> actualFile(String name, Separator separator) {
        List<File> files = new ArrayList<>();
        files.add(new File(name + separator.pure() + "anotherPapkaExFile.txt"));
        files.add(new File(name + separator.pure() + "papkaExFile.txt"));

        return files;
    }
}
