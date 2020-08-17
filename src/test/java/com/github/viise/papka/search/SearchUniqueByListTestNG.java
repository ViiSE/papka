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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SearchUniqueByListTestNG {

    private Search<List<String>, List<String>> search;

    @BeforeClass
    public void beforeClass() {
        search = new SearchUniqueByList<>();
    }

    @Test
    public void answer_withUnique() throws NotFoundException {
        List<String> list = new ArrayList<String>() {{
            add("/music/opus/opus111.mp3");
            add("/music/opus/opus109.mp3");
            add("/music/opus/opus111.mp3");
        }};

        List<String> expected = new ArrayList<String>() {{
            add("/music/opus/opus109.mp3");
        }};
        List<String> actual = search.answer(list);

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_noUnique() throws NotFoundException {
        List<String> list = new ArrayList<String>() {{
            add("/music/opus/opus111.mp3");
            add("/music/opus/opus111.mp3");
        }};

        search.answer(list);
    }
}
