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
import com.github.viise.papka.system.SeparatorUnix;

import java.io.File;
import java.util.*;

/**
 * Prepares folders as {@link Map}&lt;String, List&lt;File&gt;&gt;, where key is full folder name, and value is the list
 * of folder files.
 * @see PreparedFolders
 */
public class PreparedFoldersMapFilesRaw implements PreparedFolders<Map<String, List<File>>> {

    /**
     * {@link FoldersFile}.
     */
    private final FoldersFile<String, String> foldersFile;

    /**
     * {@link Separator}.
     */
    private final Separator separator;

    /**
     * List of all files folders.
     */
    private final List<File> files;

    /**
     * Ctor.
     * @param files List of all files folders.
     */
    public PreparedFoldersMapFilesRaw(List<File> files) {
        this(new FoldersFileName(), files);
    }

    /**
     * Ctor.
     * @param foldersFile {@link FoldersFile}.
     * @param files List of all files folders.
     */
    public PreparedFoldersMapFilesRaw(FoldersFile<String, String> foldersFile, List<File> files) {
        this(foldersFile, new SeparatorUnix(), files);
    }

    /**
     * Ctor.
     * @param foldersFile {@link FoldersFile}.
     * @param separator {@link Separator}.
     * @param files List of all files folders.
     */
    public PreparedFoldersMapFilesRaw(FoldersFile<String, String> foldersFile, Separator separator, List<File> files) {
        this.foldersFile = foldersFile;
        this.separator = separator;
        this.files = files;
    }

    @Override
    public Map<String, List<File>> preparation() {
        Map<String, List<File>> folderMap = new TreeMap<>();
        folderMap.put("/", new ArrayList<>());

        for(File file: files) {
            String fullFilename = file.getAbsolutePath();
            List<String> foldersName = foldersFile.folders(fullFilename);
            if(foldersName.size() == 1) // root
                folderMap.get("/").add(file);
            else {
                String fullFoldersPath = new NameFoldersPath(foldersName, separator).fullName();
                List<File> folderFiles = folderMap.get(fullFoldersPath);
                if(folderFiles != null) {
                    folderFiles.add(file);
                } else {
                    folderFiles = new ArrayList<>();
                    folderFiles.add(file);
                    folderMap.put(fullFoldersPath, folderFiles);
                }
            }
        }
        folderMap.values().forEach(Collections::sort);
        files.clear();

        return folderMap;
    }
}
