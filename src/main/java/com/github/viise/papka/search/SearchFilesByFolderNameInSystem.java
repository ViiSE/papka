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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search files in filesystem by folder name.
 * Regex supported.
 * @see Search
 */
public class SearchFilesByFolderNameInSystem implements Search<List<File>, String> {

    /**
     * Name of the folder to start the search from.
     */
    private final String beginWith;

    /**
     * Maximum number of levels of directories to visit.
     */
    private final int maxDepth;

    /**
     * Ctor.
     * @param beginWith Name of the folder to start the search from.
     */
    public SearchFilesByFolderNameInSystem(String beginWith) {
        this(beginWith, Integer.MAX_VALUE);
    }

    /**
     * Ctor.
     * @param beginWith Name of the folder to start the search from.
     * @param maxDepth Maximum number of levels of directories to visit. (default {@link Integer#MAX_VALUE})
     */
    public SearchFilesByFolderNameInSystem(String beginWith, int maxDepth) {
        this.beginWith = beginWith;
        this.maxDepth = maxDepth;
    }

    @Override
    public List<File> answer(String regex) throws NotFoundException {
        try {
            List<File> files = Files
                    .find(
                            Paths.get(beginWith),
                            maxDepth,
                            (path, basicFileAttributes) -> path.toFile().getParent().matches(regex) && !(path.toFile().isDirectory()))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            if(files.isEmpty())
                throw new NotFoundException("Files not found by folders name regex '" + regex + "'");

            return files;
        } catch (IOException e) {
            throw new NotFoundException(e);
        }
    }
}
