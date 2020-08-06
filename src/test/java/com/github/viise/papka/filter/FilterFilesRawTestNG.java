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

import com.github.viise.papka.search.SearchDuplicatesByList;
import com.github.viise.papka.search.SearchUniqueByList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FilterFilesRawTestNG {

    private List<String> rawFiles;
    private List<String> expected;

    @BeforeClass
    public void beforeClass() {
        rawFiles = new ArrayList<>();
        rawFiles.add("/root1.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root3.txt");
        rawFiles.add("/folder1/fl1.txt");
        rawFiles.add("/folder1/fl1.txt");
        rawFiles.add("/folder1/fl2.txt");

        expected = new ArrayList<>();
        expected.add("/root1.txt");
        expected.add("/root3.txt");
        expected.add("/folder1/fl2.txt");
    }

    @Test
    public void apply_withoutException() {
        Filter<List<String>> filter = new FilterFilesRaw(
                new SearchUniqueByList<>(),
                rawFiles);
        List<String> actual = filter.apply();

        assertEquals(actual, expected);
    }

    @Test
    public void apply_tryCallException() {
        Filter<List<String>> filter = new FilterFilesRaw(
                new SearchDuplicatesByList<>(),
                expected);
        List<String> actual = filter.apply();

        assertEquals(actual, new ArrayList<>());
    }
}
