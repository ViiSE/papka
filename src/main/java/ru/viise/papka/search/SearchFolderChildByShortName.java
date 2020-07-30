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

package ru.viise.papka.search;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.util.List;

/**
 * Search folder child by short name.
 * @param <T> Type to search for.
 * @see ru.viise.papka.search.SearchFolder
 */
public class SearchFolderChildByShortName<T> implements SearchFolder<T, String> {

    /**
     * List of child folder.
     */
    private final List<Folder<T>> children;

    /**
     * Ctor.
     * @param children List of child folder.
     */
    public SearchFolderChildByShortName(List<Folder<T>> children) {
        this.children = children;
    }

    @Override
    public Folder<T> answer(String shortName) throws NotFoundException {
        for(Folder<T> child: children) {
            if(child.shortName().equals(shortName))
                return child;
        }

        throw new NotFoundException("Folder '" + shortName + "' not found in children list.");
    }
}
