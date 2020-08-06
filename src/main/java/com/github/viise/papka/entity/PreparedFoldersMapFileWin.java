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

package com.github.viise.papka.entity;

import com.github.viise.papka.system.Separator;
import com.github.viise.papka.system.SeparatorWin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Prepares folders as {@link Map}&lt;String, {@link Folder}&lt;File&gt;&gt;, where key is full folder name, and value
 * is the folder with files of Windows.
 * @see PreparedFolders
 */
public class PreparedFoldersMapFileWin implements PreparedFolders<Map<String, Folder<File>>> {

    /**
     * {@link PreparedFolders}.
     */
    private final PreparedFolders<Map<String, List<String>>> prepFolders;

    /**
     * Exclude non-existent files?
     */
    private final boolean excludeNonExisting;

    /**
     * Raw files is unix like?
     */
    private final boolean isUnixLike;

    /**
     * Ctor.
     * @param prepFolders {@link PreparedFolders}.
     */
    public PreparedFoldersMapFileWin(PreparedFolders<Map<String, List<String>>> prepFolders) {
        this(prepFolders, false, false);
    }

    /**
     * Ctor.
     * @param prepFolders {@link PreparedFolders}.
     * @param excludeNonExisting Exclude non-existent files?
     */
    public PreparedFoldersMapFileWin(
            PreparedFolders<Map<String, List<String>>> prepFolders,
            boolean excludeNonExisting) {
        this(prepFolders, excludeNonExisting, false);
    }

    /**
     * Ctor.
     * @param prepFolders {@link PreparedFolders}.
     * @param excludeNonExisting Exclude non-existent files?
     * @param isUnixLike Raw files is unix like?
     */
    public PreparedFoldersMapFileWin(
            PreparedFolders<Map<String, List<String>>> prepFolders,
            boolean excludeNonExisting,
            boolean isUnixLike) {
        this.prepFolders = prepFolders;
        this.excludeNonExisting = excludeNonExisting;
        this.isUnixLike = isUnixLike;
    }

    @Override
    public Map<String, Folder<File>> preparation() {
        Separator separator = new SeparatorWin();
        Map<String, List<String>> prepMap = prepFolders.preparation();

        Map<String, Folder<File>> mapFile = new TreeMap<>();
        prepMap.forEach((fullFolderName, files) -> {
            Name name = new NameFolderRoot();
            if(!(fullFolderName.equals(name.fullName()))) {
                if(isUnixLike)
                    fullFolderName = fullFolderName.charAt(0) + separator.pure() + fullFolderName.substring(1);
                name = new NamePure(fullFolderName, separator);
            }

            if(folderIsExist(name)) {
                List<File> filesSystem = new ArrayList<>();
                for (String filename : files) {
                    String fullFilename = name.fullName() + separator.pure() + filename;
                    fullFilename = fullFilename.substring(2); // remove /\ in the beginning
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

        if(isUnixLike) {
            Map<String, Folder<File>> normalizeMapFile = new TreeMap<>();
            mapFile.forEach((fullFolderName, folder) -> {
                String normalizeName = fullFolderName;
                if(!(fullFolderName.equals("/"))) {
                    normalizeName = fullFolderName.charAt(0) +
                            fullFolderName.substring(1)
                                    .replace(separator.mirror().pure(), separator.pure());
                }
                normalizeMapFile.put(normalizeName, folder);
            });

            return normalizeMapFile;
        }

        return mapFile;
    }

    private boolean folderIsExist(Name folderName) {
        if(!excludeNonExisting)
            return true;
        else {
            String fullFolderName = folderName.fullName();
            if(!fullFolderName.equals(new NameFolderRoot().fullName()))
                fullFolderName = fullFolderName.substring(2); // remove /\ in the beginning
            return (Files.exists(Paths.get(fullFolderName)));
        }
    }
}
