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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Search files by regex.
 * @see ru.viise.papka.search.Search
 */
public class SearchFilesByRegex implements Search<List<String>, String> {


    private final boolean includeChildren;
    private final Folder<String> folder;

    public SearchFilesByRegex(Folder<String> folder, boolean includeChildren) {
        this.folder = folder;
        this.includeChildren = includeChildren;
    }

    public SearchFilesByRegex(Folder<String> folder) {
        this(folder, false);
    }

    @Override
    public List<String> answer(String regex) throws NotFoundException {
        List<String> files = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        if (includeChildren)
            folder.travel(folder -> files.addAll(find(pattern, folder)));
        else
            files.addAll(find(pattern, folder));

        if (files.isEmpty())
            throw new NotFoundException("Folder matches regex '" + regex + "' not contains files.");
        else
            return files;
    }

    private List<String> find(Pattern pattern, Folder<String> folder) {
        List<String> files = new ArrayList<>();

        for(String file: folder.files()) {
            if(pattern.matcher(file).matches()) {
                files.add(file);
            }
        }

        return files;
    }
}
