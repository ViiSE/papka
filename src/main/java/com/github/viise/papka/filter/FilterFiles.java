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
 * Filter files.
 * @param <T> Type of files being filtered.
 * @see Filter
 */
public class FilterFiles<T> implements Filter<List<T>> {

    /**
     * {@link Search}.
     */
    private final Search<List<T>, String> search;

    /**
     * Filter condition.
     */
    private final String regex;

    /**
     * Ctor.
     * @param search {@link Search}.
     * @param regex Filter condition.
     */
    public FilterFiles(Search<List<T>, String> search, String regex) {
        this.search = search;
        this.regex = regex;
    }

    @Override
    public List<T> apply() {
        try {
            return search.answer(regex);
        } catch (NotFoundException ex) {
            return new ArrayList<>();
        }
    }
}
