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

import ru.viise.papka.exception.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFilesInSystem implements Search<List<File>, String> {

    private final String beginWith;

    public SearchFilesInSystem(String beginWith) {
        this.beginWith = beginWith;
    }

    @Override
    public List<File> answer(String regex) throws NotFoundException {
        try {
            List<File> files = Files
                    .find(
                            Paths.get(beginWith),
                            Integer.MAX_VALUE,
                            (path, basicFileAttributes) -> path.toFile().getName().matches(regex) && !(path.toFile().isDirectory()))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            if(files.isEmpty())
                throw new NotFoundException("Files not found with regex '" + regex + "'");

            return files;
        } catch (IOException e) {
            throw new NotFoundException(e);
        }
    }
}
