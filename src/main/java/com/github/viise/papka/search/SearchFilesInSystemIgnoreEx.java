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
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Search files in system ignore exceptions (Access denied, for example).
 * Regex supported.
 * @see Search
 */
public class SearchFilesInSystemIgnoreEx implements Search<List<File>, String> {

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
    public SearchFilesInSystemIgnoreEx(String beginWith) {
        this(beginWith, Integer.MAX_VALUE);
    }

    /**
     * Ctor.
     * @param beginWith Name of the folder to start the search from.
     * @param maxDepth Maximum number of levels of directories to visit. (default {@link Integer#MAX_VALUE})
     */
    public SearchFilesInSystemIgnoreEx(String beginWith, int maxDepth) {
        this.beginWith = beginWith;
        this.maxDepth = maxDepth;
    }

    @Override
    public List<File> answer(String regex) throws NotFoundException {
        try {
            List<File> files = new ArrayList<>();
            Files.
                    walkFileTree(Paths.get(beginWith), new HashSet<>(), maxDepth, new FileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) {
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
                            if(!(basicFileAttributes.isDirectory())) {
                                if (path.toFile().getName().matches(regex))
                                    files.add(path.toFile());
                            }

                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path path, IOException e) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path path, IOException e) {
                            return FileVisitResult.CONTINUE;
                        }
                    });

            if(files.isEmpty())
                throw new NotFoundException("Files not found with regex '" + regex + "'");

            return files;
        } catch (IOException e) {
            throw new NotFoundException(e);
        }
    }
}
