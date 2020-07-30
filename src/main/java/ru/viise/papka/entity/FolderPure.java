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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Pure implementation of Folder interface.
 * @see ru.viise.papka.entity.Folder
 */
public class FolderPure<T> implements Folder<T> {

    /**
     * Folder name.
     */
    private final Name name;

    /**
     * Folder files.
     */
    private final List<T> files;

    /**
     * Folder children.
     */
    private final List<Folder<T>> children;

    /**
     * Ctor.
     * @param fullName Raw full name folder.
     * @param files List of folder files.
     */
    public FolderPure(String fullName, List<T> files) {
        this(fullName, files, new ArrayList<>());
    }

    /**
     * Ctor.
     * @param fullName Raw full name folder.
     * @param files Folder files.
     */
    @SafeVarargs
    public FolderPure(String fullName, T... files) {
        this(fullName, new ArrayList<>(Arrays.asList(files)), new ArrayList<>());
    }

    /**
     * Ctor.
     * @param fullName Raw full name folder.
     * @param files Folder files.
     * @param children Folder children.
     */
    @SafeVarargs
    public FolderPure(String fullName, List<T> files, Folder<T>... children) {
        this(new NamePure(fullName), files, new ArrayList<>(Arrays.asList(children)));
    }

    /**
     * Ctor.
     * @param fullName Raw full name folder.
     * @param files List of folder files.
     * @param children List of folder child.
     */
    public FolderPure(String fullName, List<T> files, List<Folder<T>> children) {
        this(new NamePure(fullName), files, children);
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param files List of folder files.
     */
    public FolderPure(Name name, List<T> files) {
        this(name, files, new ArrayList<>());
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param files Folder files.
     */
    @SafeVarargs
    public FolderPure(Name name, T... files) {
        this(name, new ArrayList<>(Arrays.asList(files)), new ArrayList<>());
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param files List of folder files.
     * @param children Folder children.
     */
    @SafeVarargs
    public FolderPure(Name name, List<T> files, Folder<T>... children) {
        this(name, files, new ArrayList<>(Arrays.asList(children)));
    }

    /**
     * Ctor.
     * @param name Folder name.
     * @param files List of folder files.
     * @param children List of folder child.
     */
    public FolderPure(Name name, List<T> files, List<Folder<T>> children) {
        this.name = name;
        this.files = files;
        this.children = children;
    }

    @Override
    public List<T> files() {
        return files;
    }

    @Override
    public void travel(Consumer<? super Folder<T>> folder) {
        folder.accept(this);
        for(Folder<T> child: children) {
            travel(child, folder);
        }
    }

    private void travel(Folder<T> child, Consumer<? super Folder<T>> flCons) {
        flCons.accept(child);
        for (Folder<T> _child : child.children()) {
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
    public List<Folder<T>> children() {
        return children;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object folder) {
        if(!(folder instanceof Folder)) {
            return false;
        }

        if(((Folder) folder).fullName().equals(this.fullName())) {
            if(((Folder) folder).files().equals(this.files))
                if(((Folder) folder).children().equals(this.children))
                    return true;
        }

        return super.equals(folder);
    }
}
