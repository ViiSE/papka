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

import com.github.viise.papka.entity.Folder;
import com.github.viise.papka.exception.NotFoundException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Search folder by full name.
 * @param <T> Type to search for.
 * @see SearchFolder
 */
public class SearchFolderByFullName<T> implements SearchFolder<T, String> {

    /**
     * {@link Folder} being searched.
     */
    private final Folder<T> folder;

    /**
     * Ctor.
     * @param folder {@link Folder} being searched.
     */
    public SearchFolderByFullName(Folder<T> folder) {
        this.folder = folder;
    }

    @Override
    public Folder<T> answer(String fullName) throws NotFoundException {
        if(folder.fullName().equals(fullName))
            return folder;

        AtomicReference<Folder<T>> foundFolder = new AtomicReference<>();
        AtomicBoolean isEnough = new AtomicBoolean(false);

        folder.travel(folder -> {
            if(!isEnough.get()) {
                if (folder.fullName().equals(fullName)) {
                    foundFolder.set(folder);
                    isEnough.set(true);
                }
            }
        });

        if(!isEnough.get())
            throw new NotFoundException("Folder '" + fullName + "' not found in folder '" + folder.fullName() + "'.");

        return foundFolder.get();
    }
}
