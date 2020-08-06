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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Search files by short name.
 * Regex supported.
 * @see Search
 */
public class SearchFilesByShortNameRegex implements Search<List<File>, String> {

    /**
     * To do a full depth-first search, include all children?
     */
    private final boolean includeChildren;

    /**
     * {@link Folder} being searched.
     */
    private final Folder<File> folder;

    /**
     * Ctor.
     * @param folder {@link Folder} being searched.
     * @param includeChildren To do a full depth-first search, include all children?
     */
    public SearchFilesByShortNameRegex(Folder<File> folder, boolean includeChildren) {
        this.folder = folder;
        this.includeChildren = includeChildren;
    }

    /**
     * Ctor.
     * @param folder {@link Folder} being searched.
     */
    public SearchFilesByShortNameRegex(Folder<File> folder) {
        this(folder, false);
    }

    @Override
    public List<File> answer(String regex) throws NotFoundException {
        List<File> files = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        if (includeChildren)
            folder.travel(folder -> files.addAll(findByShort(pattern, folder)));
        else
            files.addAll(findByShort(pattern, folder));

        if (files.isEmpty())
            throw new NotFoundException("Folder matches regex '" + regex + "' not contains files.");
        else
            return files;
    }

    private List<File> findByShort(Pattern pattern, Folder<File> folder) {
        List<File> files = new ArrayList<>();

        for(File file: folder.files()) {
            if(pattern.matcher(file.getName()).matches()) {
                files.add(file);
            }
        }

        return files;
    }
}
