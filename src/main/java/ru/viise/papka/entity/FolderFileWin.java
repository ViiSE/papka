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
import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorUnix;
import ru.viise.papka.system.SeparatorWin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Folder with {@link java.io.File} files from raw files for Windows.
 * @see ru.viise.papka.entity.Folder
 */
public class FolderFileWin implements Folder<File> {

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
     * Raw files.
     */
    private final List<String> rawFiles;

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
     * @param rawFiles list of raw files.
     */
    public FolderFileWin(String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)));
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles Raw files.
     */
    public FolderFileWin(Name name, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), false, false);
    }

    /**
     * Ctor.
     * @param excludeNonExisting Exclude non-existent files?
     * @param rawFiles Raw files.
     */
    public FolderFileWin(boolean excludeNonExisting, String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting, false);
    }

    /**
     * Ctor.
     * @param excludeNonExisting Exclude non-existent files?
     * @param isUnixLike Raw files is unix like?
     * @param rawFiles Raw files.
     */
    public FolderFileWin(boolean excludeNonExisting, boolean isUnixLike, String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting, isUnixLike);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param excludeNonExisting Exclude non-existent files?
     * @param rawFiles Raw files.
     */
    public FolderFileWin(Name name, boolean excludeNonExisting, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting, false);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param excludeNonExisting Exclude non-existent files?
     * @param isUnixLike Raw files is unix like?
     * @param rawFiles Raw files.
     */
    public FolderFileWin(Name name, boolean excludeNonExisting, boolean isUnixLike, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting, isUnixLike);
    }

    /**
     * Ctor.
     * @param rawFiles List of raw files.
     */
    public FolderFileWin(List<String> rawFiles) {
        this(new NameFolderRoot(), rawFiles);
    }

    /**
     * Ctor.
     * @param rawFiles List of raw files.
     * @param excludeNonExisting Exclude non-existent files?
     */
    public FolderFileWin(List<String> rawFiles, boolean excludeNonExisting) {
        this(new NameFolderRoot(), rawFiles, excludeNonExisting, false);
    }

    /**
     * Ctor.
     * @param rawFiles List of raw files.
     * @param excludeNonExisting Exclude non-existent files?
     * @param isUnixLike Raw files is unix like?
     */
    public FolderFileWin(List<String> rawFiles, boolean excludeNonExisting, boolean isUnixLike) {
        this(new NameFolderRoot(), rawFiles, excludeNonExisting, isUnixLike);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles List of raw files.
     */
    public FolderFileWin(Name name, List<String> rawFiles) {
        this(name, rawFiles, false, false);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles List of raw files.
     * @param excludeNonExisting Exclude non-existent files?
     */
    public FolderFileWin(Name name, List<String> rawFiles, boolean excludeNonExisting) {
        this(name, rawFiles, excludeNonExisting, false);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles List of raw files.
     * @param excludeNonExisting Exclude non-existent files?
     * @param isUnixLike Raw files is unix like?
     */
    public FolderFileWin(Name name, List<String> rawFiles, boolean excludeNonExisting, boolean isUnixLike) {
        this.name = name;
        this.rawFiles = rawFiles;
        this.excludeNonExisting = excludeNonExisting;
        this.isUnixLike = isUnixLike;
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

        Separator rawSeparator = isUnixLike ? new SeparatorUnix() : new SeparatorWin();
        FoldersFile<String, String> fsWin = new FoldersFileNameWin(rawSeparator);

        TreeFolder<File> trFolder = new TreeFolderPure<>(
                new FoldersFileNameWin(new SeparatorWin()),
                new PreparedFoldersMapFileWin(
                        new PreparedFoldersMapRaw(
                                fsWin,
                                rawSeparator,
                                rawFiles
                        ),
                        excludeNonExisting,
                        isUnixLike
                ),
                new SeparatorWin());

        Folder<File> prepRoot = trFolder.grow();
        try {
            String fullName = name.fullName();
            if(fullName.charAt(0) != '/')
                fullName = '/' + new SeparatorWin().pure() + fullName;
            name = new NamePure(fullName, new SeparatorWin());

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
