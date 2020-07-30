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
 * Folder with {@link java.lang.String} files from raw files.
 * @see ru.viise.papka.entity.Folder
 */
public class FolderText implements Folder<String> {

    /**
     * Folder name.
     */
    private final Name name;

    /**
     * Folder files.
     */
    private List<String> files;

    /**
     * Folder children.
     */
    private List<Folder<String>> children;

    /**
     * {@link TreeFolder}.
     */
    private final TreeFolder<String> trFolder;

    /**
     * Ctor.
     * @param rawFiles List of raw files.
     */
    public FolderText(List<String> rawFiles) {
        this(new NameFolderRoot(), rawFiles);
    }

    /**
     * Ctor.
     * @param rawFiles Raw files.
     */
    public FolderText(String... rawFiles) {
        this(new NameFolderRoot(), new TreeFolderPure<>(
                new PreparedFoldersMapText(
                        new PreparedFoldersMapRaw(new ArrayList<>(Arrays.asList(rawFiles))))
        ));
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles Raw files.
     */
    public FolderText(Name name, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)));
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param rawFiles List of raw files.
     */
    public FolderText(Name name, List<String> rawFiles) {
        this(name, new TreeFolderPure<>(
                new PreparedFoldersMapText(
                        new PreparedFoldersMapRaw(rawFiles))
        ));
    }


    /**
     * Ctor.
     * @param name Folder name.
     * @param trFolder {@link TreeFolder}.
     */
    public FolderText(Name name, TreeFolder<String> trFolder) {
        this.name = name;
        this.trFolder = trFolder;
    }

    @Override
    public List<String> files() {
        if(files == null)
            init();
        return files;
    }

    private void init() {
        files = new ArrayList<>();
        children = new ArrayList<>();
        Folder<String> prepRoot = trFolder.grow();
        try {
            Folder<String> searchFolder = new SearchFolderByFullName<>(prepRoot).answer(name.fullName());
            this.files = searchFolder.files();
            this.children = searchFolder.children();
        } catch (NotFoundException ex) {
            this.files = prepRoot.files();
            this.children = prepRoot.children();
        }
    }

    @Override
    public void travel(Consumer<? super Folder<String>> folder) {
        if(files == null)
            init();
        folder.accept(this);
        for(Folder<String> child: children) {
            travel(child, folder);
        }
    }

    private void travel(Folder<String> child, Consumer<? super Folder<String>> flCons) {
        flCons.accept(child);
        for (Folder<String> _child : child.children()) {
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
    public List<Folder<String>> children() {
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

        if(((Folder) folder).fullName().equals(this.fullName())) {
            if(((Folder) folder).files().equals(files()))
                if(((Folder) folder).children().equals(children()))
                return true;
        }

        return super.equals(folder);
    }
}
