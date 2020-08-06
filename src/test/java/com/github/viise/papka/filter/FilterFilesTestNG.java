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

package com.github.viise.papka.filter;

import com.github.viise.papka.entity.Folder;
import com.github.viise.papka.entity.FolderPure;
import com.github.viise.papka.entity.NameFolderRoot;
import com.github.viise.papka.search.SearchByStartWith;
import com.github.viise.papka.search.SearchFilesByFolderNameRegex;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FilterFilesTestNG {

    private Folder<String> folder;

    @BeforeClass
    public void beforeClass() {
        List<String> rootFiles = new ArrayList<>();
        rootFiles.add("root1.png");
        rootFiles.add("root2.pdf");

        List<String> child1 = new ArrayList<>();
        child1.add("child1.txt");
        child1.add("child1.png");

        List<String> child2 = new ArrayList<>();
        child2.add("child2.txt");
        child2.add("child2.png");

        List<String> child2_1 = new ArrayList<>();
        child2_1.add("child2_1.txt");
        child2_1.add("child2_1.png");

        folder = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new FolderPure<>(
                        "/child1",
                        child1),
                new FolderPure<>(
                        "/child2",
                        child2,
                        new FolderPure<>(
                                "/child1/child2_1",
                                child2_1))
        );
    }

    @Test
    public void apply_withoutException() {
        Filter<List<String>> filter = new FilterFiles<>(
                new SearchByStartWith<>(
                        new SearchFilesByFolderNameRegex<>(folder, false)),
                "child2");
        List<String> actual = filter.apply();

        List<String> expected = new ArrayList<>();
        expected.add("child2.txt");
        expected.add("child2.png");
        expected.add("child2_1.txt");
        expected.add("child2_1.png");

        assertEquals(actual, expected);
    }

    @Test
    public void apply_tryCallException() {
        Filter<List<String>> filter = new FilterFiles<>(
                new SearchByStartWith<>(
                        new SearchFilesByFolderNameRegex<>(folder)),
                "music");

        List<String> actual = filter.apply();
        assertEquals(actual, new ArrayList<>());
    }
}
