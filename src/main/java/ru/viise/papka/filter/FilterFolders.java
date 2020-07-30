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

package ru.viise.papka.filter;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.search.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter folders.
 * @param <T> Type of folders files being filtered.
 * @see ru.viise.papka.filter.Filter
 */
public class FilterFolders<T> implements Filter<List<Folder<T>>> {

    /**
     * {@link Search}.
     */
    private final Search<List<Folder<T>>, String> searchFolders;

    /**
     * Filter condition.
     */
    private final String pattern;

    /**
     * Ctor.
     * @param searchFolders {@link Search}.
     * @param pattern Filter condition.
     */
    public FilterFolders(Search<List<Folder<T>>, String> searchFolders, String pattern) {
        this.searchFolders = searchFolders;
        this.pattern = pattern;
    }

    @Override
    public List<Folder<T>> apply() {
        try {
            return searchFolders.answer(pattern);
        } catch (NotFoundException ex) {
            return new ArrayList<>();
        }
    }
}
