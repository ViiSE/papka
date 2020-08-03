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

import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorUnix;

import java.util.*;

/**
 * Prepares folders as {@link Map}&lt;String, List&lt;String&gt;&gt;, where key is full folder name, and value is the
 * list of short folder filenames.
 * @see ru.viise.papka.entity.PreparedFolders
 */
public class PreparedFoldersMapRaw implements PreparedFolders<Map<String, List<String>>> {

    /**
     * {@link FolderFile}.
     */
    private final FoldersFile<String, String> foldersFile;

    /**
     * {@link Separator}.
     */
    private final Separator separator;

    /**
     * List of raw files.
     */
    private final List<String> rawFiles;

    /**
     * Ctor.
     * @param rawFiles List of raw files.
     */
    public PreparedFoldersMapRaw(List<String> rawFiles) {
        this(new FoldersFileName(), rawFiles);
    }

    /**
     * Ctor.
     * @param foldersFile {@link FoldersFile}.
     * @param rawFiles List of folders file.
     */
    public PreparedFoldersMapRaw(FoldersFile<String, String> foldersFile, List<String> rawFiles) {
        this(foldersFile, new SeparatorUnix(), rawFiles);
    }

    /**
     * Ctor.
     * @param foldersFile {@link FoldersFile}.
     * @param separator {@link Separator}.
     * @param rawFiles List of folders file.
     */
    public PreparedFoldersMapRaw(FoldersFile<String, String> foldersFile, Separator separator, List<String> rawFiles) {
        this.foldersFile = foldersFile;
        this.separator = separator;
        this.rawFiles = rawFiles;
    }

    @Override
    public Map<String, List<String>> preparation() {
        Map<String, List<String>> folderMap = new TreeMap<>();
        folderMap.put("/", new ArrayList<>());

        for(String fullFilename: rawFiles) {
            String shortFilename = new NamePure(fullFilename, separator).shortName();
            List<String> foldersName = foldersFile.folders(fullFilename);
            if(foldersName.size() == 1) // root
                folderMap.get("/").add(shortFilename);
            else {
                String fullFoldersPath = new NameFoldersPath(foldersName, separator).fullName();
                List<String> files = folderMap.get(fullFoldersPath);
                if(files != null) {
                    files.add(shortFilename);
                } else {
                    files = new ArrayList<>();
                    files.add(shortFilename);
                    folderMap.put(fullFoldersPath, files);
                }
            }
        }
        folderMap.values().forEach(Collections::sort);
        rawFiles.clear();

        return folderMap;
    }
}
