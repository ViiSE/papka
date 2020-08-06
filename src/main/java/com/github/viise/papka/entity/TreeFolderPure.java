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

import com.github.viise.papka.search.SearchFolderChildByShortName;
import com.github.viise.papka.system.Separator;
import com.github.viise.papka.system.SeparatorUnix;
import com.github.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Pure implementation of TreeFolder interface.
 * @param <T> Type of folder files.
 * @see TreeFolder
 */
public class TreeFolderPure<T> implements TreeFolder<T> {

    /**
     * {@link FoldersFile}.
     */
    private final FoldersFile<String, String> foldersFile;

    /**
     * {@link PreparedFolders}.
     */
    private final PreparedFolders<Map<String, Folder<T>>> prepFolders;

    /**
     * {@link Separator}.
     */
    private final Separator separator;

    /**
     * Ctor.
     * @param prepFolders {@link PreparedFolders}.
     */
    public TreeFolderPure(PreparedFolders<Map<String, Folder<T>>> prepFolders) {
        this(new FoldersFileName(), prepFolders);
    }

    /**
     * Ctor.
     * @param foldersFile {@link FoldersFile}.
     * @param prepFolders {@link PreparedFolders}.
     */
    public TreeFolderPure(
            FoldersFile<String, String> foldersFile,
            PreparedFolders<Map<String, Folder<T>>> prepFolders) {
        this(foldersFile, prepFolders, new SeparatorUnix());
    }

    /**
     * Ctor.
     * @param foldersFile {@link FoldersFile}.
     * @param prepFolders {@link PreparedFolders}.
     * @param separator {@link Separator}.
     */
    public TreeFolderPure(
            FoldersFile<String, String> foldersFile,
            PreparedFolders<Map<String, Folder<T>>> prepFolders,
            Separator separator) {
        this.foldersFile = foldersFile;
        this.prepFolders = prepFolders;
        this.separator = separator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Folder<T> grow() {
        Map<String, Folder<T>> folderMap = prepFolders.preparation();
        Folder<T> root = (Folder<T>) folderMap.values().toArray()[0];

        folderMap.remove(root.fullName());
        AtomicReference<Folder<T>> prevFolder = new AtomicReference<>(root);

        folderMap.forEach((fullName, folder) -> {
            if (folder.shortName().equals(fullName.substring(1, fullName.length() - 1))) {
                root.children().add(folder);
            } else {
                List<String> foldersNames = foldersFile.folders(fullName);
                foldersNames = foldersNames.subList(1, foldersNames.size());
                addFolder(root.children(), folder, foldersNames);
            }
            prevFolder.set(folder);
        });

        folderMap.clear();

        return root;
    }

    private void addFolder(List<Folder<T>> rootChildren, Folder<T> folder, List<String> foldersName) {
        // First folder is root child
        Folder<T> root;
        try {
            root = new SearchFolderChildByShortName<>(rootChildren).answer(foldersName.get(0));
        } catch (NotFoundException ex) {
            String rootName = "/" + getName(foldersName.get(0));
            root = new FolderPure<>(new NamePure(rootName, separator), new ArrayList<>());
            rootChildren.add(root);
        }

        String fullName = root.fullName();
        for (int i = 1; i < foldersName.size(); i++) {
            String shortName = foldersName.get(i);
            fullName += getName(foldersName.get(i));
            try {
                root = new SearchFolderChildByShortName<>(root.children()).answer(shortName);
            } catch (NotFoundException ex) {
                Folder<T> child = new FolderPure<>(new NamePure(fullName, separator), new ArrayList<>());
                root.children().add(child);
                root = child;
            }
        }

        // Last folder is target child
        root.files().addAll(folder.files());
    }

    private String getName(String rawName) {
        return separator.pure() + rawName;
    }
}
