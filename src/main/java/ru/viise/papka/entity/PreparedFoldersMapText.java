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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Prepares folders as {@link Map}<String, Folder<String>>, where key is full folder name, and value is the folder with
 * files like short filenames.
 * @see ru.viise.papka.entity.PreparedFolders
 */
public class PreparedFoldersMapText implements PreparedFolders<Map<String, Folder<String>>> {

    /**
     * {@link PreparedFolders}.
     */
    private final PreparedFolders<Map<String, List<String>>> prepFolders;

    /**
     * Ctor.
     * @param prepFolders {@link PreparedFolders}.
     */
    public PreparedFoldersMapText(PreparedFolders<Map<String, List<String>>> prepFolders) {
        this.prepFolders = prepFolders;
    }

    @Override
    public Map<String, Folder<String>> preparation() {
        Map<String, List<String>> prepMap = prepFolders.preparation();

        Map<String, Folder<String>> mapText = new TreeMap<>();
        prepMap.forEach((fullFolderName, files) -> {
            Name name;
            if(fullFolderName.equals("/"))
                name = new NameFolderRoot();
            else
                name = new NamePure(fullFolderName);
            mapText.put(fullFolderName, new FolderPure<>(name, files));
        });
        return mapText;
    }
}
