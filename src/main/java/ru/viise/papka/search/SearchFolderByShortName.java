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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Search folder by short name.
 * @param <T> Type to search for.
 * @see ru.viise.papka.search.SearchFolder
 */
public class SearchFolderByShortName<T> implements SearchFolder<T, String> {

    /**
     * {@link Folder} being searched.
     */
    private final Folder<T> folder;

    /**
     * Search folder to end of folder tree or search to first match with search criteria?
     */
    private final boolean isLast;

    /**
     * Ctor.
     * @param folder {@link Folder} being searched.
     */
    public SearchFolderByShortName(Folder<T> folder) {
        this(folder, false);
    }

    /**
     * Ctor.
     * @param folder {@link Folder} being searched.
     * @param isLast Search folder to end of folder tree or search to first match with search criteria?
     */
    public SearchFolderByShortName(Folder<T> folder, boolean isLast) {
        this.folder = folder;
        this.isLast = isLast;
    }

    @Override
    public Folder<T> answer(String shortName) throws NotFoundException {
        if(!isLast) {
            if (folder.shortName().equals(shortName))
                return folder;
        }

        AtomicReference<Folder<T>> foundFolder = new AtomicReference<>();
        AtomicBoolean isEnough = new AtomicBoolean(false);

        folder.travel(folder -> {
            if(isLast) {
                if (folder.shortName().equals(shortName))
                    foundFolder.set(folder);
            } else if(!isEnough.get()) {
                if (folder.shortName().equals(shortName)) {
                    foundFolder.set(folder);
                    isEnough.set(true);
                }
            }
        });

        if(!isLast) {
            if (!isEnough.get())
                throw exception(shortName, folder.fullName());
        } else if(foundFolder.get() == null)
            throw exception(shortName, folder.fullName());

        return foundFolder.get();
    }

    private NotFoundException exception(String shortName, String fullName) {
        return new NotFoundException("Folder '" + shortName + "' not found in folder '" + fullName + "'.");
    }
}
