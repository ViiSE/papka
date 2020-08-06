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

import com.github.viise.papka.entity.Folder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.viise.papka.entity.FolderPure;
import com.github.viise.papka.entity.NameFolderRoot;
import com.github.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class SearchByExtTestNG {

    private Search<List<String>, String> search;

    @BeforeClass
    public void beforeClass() {
        List<String> rootFiles = new ArrayList<>();
        rootFiles.add("root1.png");
        rootFiles.add("root2.pdf");
        rootFiles.add("child1.txt");
        rootFiles.add("child1.png");
        rootFiles.add("child2.txt");
        rootFiles.add("child2.png");
        rootFiles.add("child2_1.txt");
        rootFiles.add("child2_1.png");

        Folder<String> root = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles);

        search = new SearchByExt<>(
                new SearchFilesByRegex(root, false));
    }

    @Test
    public void answer() throws NotFoundException {
        List<String> actual = search.answer(".txt");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("child1.txt");
        expected.add("child2.txt");
        expected.add("child2_1.txt");

        assertEquals(expected, actual);
    }

    @Test
    public void answer_multipleExt() throws NotFoundException {
        List<String> actual = search.answer(".pdf|.png");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("root1.png");
        expected.add("root2.pdf");
        expected.add("child1.png");
        expected.add("child2.png");
        expected.add("child2_1.png");


        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        search.answer(".jpeg");
    }
}
