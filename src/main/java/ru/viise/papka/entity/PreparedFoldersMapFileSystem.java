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

package ru.viise.papka.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Prepares folders as {@link Map}<String, {@link Folder}<File>>, where key is full folder name, and value is the list
 * of folder files.
 * @see ru.viise.papka.entity.PreparedFolders
 */
public class PreparedFoldersMapFileSystem implements PreparedFolders<Map<String, Folder<File>>> {

    /**
     * {@link PreparedFolders}.
     */
    private final PreparedFolders<Map<String, List<File>>> prepFolders;

    /**
     * Ctor.
     * @param prepFolders {@link PreparedFolders}.
     */
    public PreparedFoldersMapFileSystem(
            PreparedFolders<Map<String, List<File>>> prepFolders) {
        this.prepFolders = prepFolders;
    }

    @Override
    public Map<String, Folder<File>> preparation() {
        Map<String, List<File>> prepMap = prepFolders.preparation();

        Map<String, Folder<File>> mapFile = new TreeMap<>();
        prepMap.forEach((fullFolderName, files) -> {
            Name name = new NameFolderRoot();
            if(!(fullFolderName.equals(name.fullName()))) {
                name = new NamePure(fullFolderName);
            }

            List<File> filesSystem = new ArrayList<>(files);
            mapFile.put(fullFolderName, new FolderPure<>(name, filesSystem));
        });
        return mapFile;
    }
}
