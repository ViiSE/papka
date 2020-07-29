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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PreparedFoldersMapFile implements PreparedFolders<Map<String, Folder<File>>> {

    private final PreparedFolders<Map<String, List<String>>> prepFolders;
    private final boolean excludeNonExisting;

    public PreparedFoldersMapFile(PreparedFolders<Map<String, List<String>>> prepFolders) {
        this(prepFolders, false);
    }

    public PreparedFoldersMapFile(
            PreparedFolders<Map<String, List<String>>> prepFolders,
            boolean excludeNonExisting) {
        this.prepFolders = prepFolders;
        this.excludeNonExisting = excludeNonExisting;
    }

    @Override
    public Map<String, Folder<File>> preparation() {
        Map<String, List<String>> prepMap = prepFolders.preparation();

        Map<String, Folder<File>> mapFile = new TreeMap<>();
        prepMap.forEach((fullFolderName, files) -> {
            Name name = new NameFolderRoot();
            if(!(fullFolderName.equals(name.fullName()))) {
                name = new NamePure(fullFolderName);
            }

            if(folderIsExist(name)) {
                List<File> filesSystem = new ArrayList<>();
                for (String filename : files) {
                    String fullFilename = fullFolderName + filename;
                    File file = new File(fullFilename);
                    if (excludeNonExisting) {
                        if (file.exists())
                            filesSystem.add(file);
                    } else
                        filesSystem.add(file);
                }
                mapFile.put(fullFolderName, new FolderPure<>(name, filesSystem));
            }
        });
        return mapFile;
    }

    private boolean folderIsExist(Name folderName) {
        if(!excludeNonExisting)
            return true;
        else {
            String fullFolderName = folderName.fullName();
            return (Files.exists(Paths.get(fullFolderName)));
        }
    }
}
