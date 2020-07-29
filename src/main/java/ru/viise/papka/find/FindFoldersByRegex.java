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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindFoldersByRegex<T> implements FindFolders<T, String> {

    private final Folder<T> folder;
    private final boolean isFullName;

    public FindFoldersByRegex(Folder<T> folder, boolean isFullName) {
        this.folder = folder;
        this.isFullName = isFullName;
    }

    public FindFoldersByRegex(Folder<T> folder) {
        this(folder, true);
    }

    @Override
    public List<Folder<T>> answer(String regex) throws NotFoundException {
        List<Folder<T>> folders = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        folder.travel(folder -> {
            try {
                folders.add(findFolder(pattern, folder));
            } catch (NotFoundException ignore) {}
        });

        if(folders.isEmpty())
            throw new NotFoundException("Folders matches regex '" + regex + "' not found in folder " + folder.fullName() + ".");
        else
            return folders;
    }

    private Folder<T> findFolder(Pattern pattern, Folder<T> child) throws NotFoundException {
        String name = child.fullName();
        if(!isFullName)
            name = child.shortName();
        if(pattern.matcher(name).matches())
            return child;

        throw new NotFoundException("Not found. Ignore");
    }
}
