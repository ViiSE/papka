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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.viise.papka.entity.Folder;
import com.github.viise.papka.entity.FolderPure;
import com.github.viise.papka.entity.NameFolderRoot;
import com.github.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class SearchFilesByRegexTestNG {

    private Folder<String> root;

    @BeforeClass
    public void beforeClass() {
        List<String> rootFiles = new ArrayList<>();
        rootFiles.add("root1.png");
        rootFiles.add("root2.pdf");
        rootFiles.add("childRoot1.pdf");
        rootFiles.add("childRoot2.pdf");

        List<String> child1Files = new ArrayList<>();
        child1Files.add("child1.txt");
        child1Files.add("child1.png");

        List<String> child2Files = new ArrayList<>();
        child2Files.add("child2.txt");
        child2Files.add("child2.png");

        List<String> child21Files = new ArrayList<>();
        child21Files.add("child21.txt");
        child21Files.add("child21.png");

        root = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new FolderPure<>(
                        "/child1",
                        child1Files),
                new FolderPure<>(
                        "/child2",
                        child2Files),
                new FolderPure<>(
                        "/child21",
                        child21Files));
    }

    @Test
    public void answer_excludeChildren() throws NotFoundException {
        Search<List<String>, String> search = new SearchFilesByRegex(root);

        List<String> actual = search.answer("^child.*$");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("childRoot1.pdf");
        expected.add("childRoot2.pdf");

        assertEquals(expected, actual);
    }

    @Test
    public void answer_includeChildren() throws NotFoundException {
        Search<List<String>, String> search = new SearchFilesByRegex(root, true);

        List<String> actual = search.answer("^child.*$");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("childRoot1.pdf");
        expected.add("childRoot2.pdf");
        expected.add("child1.txt");
        expected.add("child1.png");
        expected.add("child2.txt");
        expected.add("child2.png");
        expected.add("child21.txt");
        expected.add("child21.png");

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        Search<List<String>, String> search = new SearchFilesByRegex(root, true);
        search.answer("opus");
    }
}
