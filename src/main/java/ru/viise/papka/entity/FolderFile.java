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

import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.search.SearchFolderByFullName;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Folder with {@link java.io.File} files from raw files.
 * @see ru.viise.papka.entity.Folder
 */
public class FolderFile implements Folder<File> {

    /**
     * Folder name.
     */
    private Name name;

    /**
     * Folder files.
     */
    private List<File> files;

    /**
     * Folder children.
     */
    private List<Folder<File>> children;

    /**
     * {@link TreeFolder}.
     */
    private final TreeFolder<File> trFolder;

    /**
     * Ctor.
     * @param rawFiles List of raw files.
     */
    public FolderFile(String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)));
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles Raw files.
     */
    public FolderFile(Name name, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), false);
    }

    /**
     * Ctor.
     * @param excludeNonExisting Exclude non-existent files?
     * @param rawFiles Raw files.
     */
    public FolderFile(boolean excludeNonExisting, String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param excludeNonExisting Exclude non-existent files?
     * @param rawFiles Raw files.
     */
    public FolderFile(Name name, boolean excludeNonExisting, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), new FoldersFileName(), excludeNonExisting);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param foldersFile {@link FoldersFile}.
     * @param excludeNonExisting Exclude non-existent files?
     * @param rawFiles Raw files.
     */
    public FolderFile(Name name, FoldersFile<String, String> foldersFile, boolean excludeNonExisting, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), foldersFile, excludeNonExisting);
    }

    /**
     * Ctor.
     * @param rawFiles List of raw files.
     */
    public FolderFile(List<String> rawFiles) {
        this(new NameFolderRoot(), rawFiles);
    }

    /**
     * Ctor.
     * @param rawFiles List of raw files.
     * @param excludeNonExisting Exclude non-existent files?
     */
    public FolderFile(List<String> rawFiles, boolean excludeNonExisting) {
        this(new NameFolderRoot(), rawFiles, excludeNonExisting);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles List of raw files.
     */
    public FolderFile(Name name, List<String> rawFiles) {
        this(name, rawFiles, false);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles List of raw files.
     * @param excludeNonExisting Exclude non-existent files?
     */
    public FolderFile(Name name, List<String> rawFiles, boolean excludeNonExisting) {
        this(name, rawFiles, new FoldersFileName(), excludeNonExisting);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles List of raw files.
     * @param foldersFile {@link FoldersFile}.
     * @param excludeNonExisting Exclude non-existent files?
     */
    public FolderFile(Name name, List<String> rawFiles, FoldersFile<String, String> foldersFile, boolean excludeNonExisting) {
        this(name, new TreeFolderPure<>(
                foldersFile,
                new PreparedFoldersMapFile(
                        new PreparedFoldersMapRaw(
                                foldersFile,
                                rawFiles),
                        excludeNonExisting)
        ));
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param trFolder {@link TreeFolder}
     */
    public FolderFile(Name name, TreeFolder<File> trFolder) {
        this.name = name;
        this.trFolder = trFolder;
    }

    @Override
    public List<File> files() {
        if(files == null)
            init();
        return files;
    }

    private void init() {
        files = new ArrayList<>();
        children = new ArrayList<>();
        Folder<File> prepRoot = trFolder.grow();
        try {
            Folder<File> searchFolder = new SearchFolderByFullName<>(prepRoot).answer(name.fullName());
            this.files = searchFolder.files();
            this.children = searchFolder.children();
        } catch (NotFoundException ex) {
            this.name = new NameFolderRoot();
            this.files = prepRoot.files();
            this.children = prepRoot.children();
        }
    }

    @Override
    public void travel(Consumer<? super Folder<File>> folder) {
        if(files == null)
            init();
        folder.accept(this);
        for(Folder<File> child: children) {
            travel(child, folder);
        }
    }

    private void travel(Folder<File> child, Consumer<? super Folder<File>> flCons) {
        flCons.accept(child);
        for (Folder<File> _child : child.children()) {
            travel(_child, flCons);
        }
    }

    @Override
    public String shortName() {
        return name.shortName();
    }

    @Override
    public String fullName() {
        return name.fullName();
    }

    @Override
    public List<Folder<File>> children() {
        if(children == null)
            init();
        return children;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object folder) {
        if(!(folder instanceof Folder)) {
            return false;
        }

        List filesF = ((Folder) folder).files();
        files();

        List childrenF = ((Folder) folder).children();
        children();

        if(((Folder) folder).fullName().equals(this.fullName())) {
            if(filesF.equals(this.files))
                if(childrenF.equals(this.children))
                return true;
        }

        return super.equals(folder);
    }
}
