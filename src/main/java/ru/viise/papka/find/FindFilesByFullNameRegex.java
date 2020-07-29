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

package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindFilesByFullNameRegex implements Find<List<File>, String> {

    private final boolean includeChildren;
    private final Folder<File> folder;

    public FindFilesByFullNameRegex(Folder<File> folder, boolean includeChildren) {
        this.folder = folder;
        this.includeChildren = includeChildren;
    }

    public FindFilesByFullNameRegex(Folder<File> folder) {
        this(folder, false);
    }

    @Override
    public List<File> answer(String regex) throws NotFoundException {
        List<File> files = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        if (includeChildren)
            folder.travel(folder -> files.addAll(findByAbsolutePath(pattern, folder)));
        else
            files.addAll(findByAbsolutePath(pattern, folder));

        if (files.isEmpty())
            throw new NotFoundException("Folder matches regex '" + regex + "' not contains files.");
        else
            return files;
    }

    private List<File> findByAbsolutePath(Pattern pattern, Folder<File> folder) {
        List<File> files = new ArrayList<>();

        for(File file: folder.files()) {
            String absolutePath = file.getAbsolutePath();
            absolutePath = absolutePath.replace("\\", "\\\\");
            if(pattern.matcher(absolutePath).matches()) {
                files.add(file);
            }
        }

        return files;
    }
}
