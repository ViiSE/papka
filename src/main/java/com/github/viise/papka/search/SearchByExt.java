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

/**
 * Search by extension.
 * @param <T> Type to search for.
 * @see Search
 */
public class SearchByExt<T> implements Search<T, String> {

    /**
     * {@link Search}.
     */
    private final Search<T, String> search;

    /**
     * Ctor.
     * @param search {@link Search}.
     */
    public SearchByExt(Search<T, String> search) {
        this.search = search;
    }

    @Override
    public T answer(String ext) throws NotFoundException {
        String extReg = ext.replaceAll("\\.", "\\\\.");
        return search.answer("([^.]*)(" + extReg + "$)");
    }
}
