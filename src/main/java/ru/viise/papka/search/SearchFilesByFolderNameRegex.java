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

public class SearchFilesByFolderNameRegex<T> implements Search<List<T>, String> {

    private final Folder<T> folder;
    private final boolean isFullName;

    public SearchFilesByFolderNameRegex(
            Folder<T> folder,
            boolean isFullName) {
        this.folder = folder;
        this.isFullName = isFullName;
    }

    public SearchFilesByFolderNameRegex(Folder<T> folder) {
        this(folder, true);
    }

    @Override
    public List<T> answer(String regex) throws NotFoundException {
        List<T> files = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        folder.travel(folder -> files.addAll(findFolder(pattern, folder)));

        if (files.isEmpty())
            throw new NotFoundException("Folder matches regex '" + regex + "' not contains files.");
        else
            return files;
    }

    private List<T> findFolder(Pattern pattern, Folder<T> folder) {
        if (isFullName)
            return matchesName(pattern, folder, folder.fullName());
        else
            return matchesName(pattern, folder, folder.shortName());
    }

    private List<T> matchesName(Pattern pattern, Folder<T> folder, String name) {
        if (pattern.matcher(name).matches())
            return folder.files();
        return new ArrayList<>();
    }
}