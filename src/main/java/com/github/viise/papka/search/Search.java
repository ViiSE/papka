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
 * Searches for both files and folders.
 * @param <T> Type to search for.
 * @param <V> Type of search condition.
 */
public interface Search<T, V> {

    /**
     * @param query Search condition.
     * @return Search result.
     * @throws NotFoundException If search did not return any result.
     */
    T answer(V query) throws NotFoundException;
}
