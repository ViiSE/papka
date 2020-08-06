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

import com.github.viise.papka.search.Search;
import com.github.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter files like filename.
 * @see Filter
 */
public class FilterFilesRaw implements Filter<List<String>> {

    /**
     * {@link Search}.
     */
    private final Search<List<String>, List<String>> search;

    /**
     * List of raw files.
     */
    private final List<String> rawFiles;

    /**
     * Ctor.
     * @param search {@link Search}.
     * @param rawFiles List of raw files.
     */
    public FilterFilesRaw(Search<List<String>, List<String>> search, List<String> rawFiles) {
        this.search = search;
        this.rawFiles = rawFiles;
    }

    @Override
    public List<String> apply() {
        try {
            return search.answer(rawFiles);
        } catch (NotFoundException ex) {
            return new ArrayList<>();
        }
    }
}
