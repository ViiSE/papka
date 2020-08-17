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
import com.github.viise.papka.entity.FolderPure;
import com.github.viise.papka.entity.NameFolderRoot;
import com.github.viise.papka.exception.NotFoundException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SearchFolderByShortNameTestNG {

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

        List<String> innerChild1Files = new ArrayList<>();
        innerChild1Files.add("innerChild1.txt");
        innerChild1Files.add("innerChild1.png");

        root = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new FolderPure<>(
                        "/child1",
                        child1Files),
                new FolderPure<>(
                        "/child2",
                        child2Files,
                        new FolderPure<>(
                                "/child2/child21",
                                child21Files),
                        new FolderPure<>(
                                "/child2/child1",
                                innerChild1Files)));
    }

    @Test
    public void answer_found() throws NotFoundException {
        SearchFolder<String, String> search = new SearchFolderByShortName<>(root);
        Folder<String> actual = search.answer("child1");

        Folder<String> expected = new FolderPure<>(
                "/child1",
                new ArrayList<String>() {{
                    add("child1.txt");
                    add("child1.png");
                }});

        assertEquals(expected, actual);
    }

    @Test
    public void answer_fastFind() throws NotFoundException {
        SearchFolder<String, String> search = new SearchFolderByShortName<>(root);
        Folder<String> actual = search.answer("/");

        assertEquals(root, actual);
    }

    @Test
    public void answer_foundLast() throws NotFoundException {
        SearchFolder<String, String> search = new SearchFolderByShortName<>(root, true);
        Folder<String> actual = search.answer("child1");

        Folder<String> expected = new FolderPure<>(
                "/child2/child1",
                new ArrayList<String>() {{
                    add("innerChild1.txt");
                    add("innerChild1.png");
                }});

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFoundLast() throws NotFoundException {
        SearchFolder<String, String> search = new SearchFolderByShortName<>(root, true);
        search.answer("/music");
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        SearchFolder<String, String> search = new SearchFolderByShortName<>(root);
        search.answer("/music");
    }
}
