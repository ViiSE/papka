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

import java.util.List;
import java.util.function.Consumer;

/**
 * Folder contains files and children. Children is another Folder, which can be obtained with {@link #children()} method
 * from {@link Node} interface, or can be reached with {@link #travel(Consumer)} method.
 * @param <T> Type of folder file.
 */
public interface Folder<T> extends Node<Folder<T>>, Name {

    /**
     * @return List of files folder.
     */
    List<T> files();

    /**
     * Traversal of the file tree, starting at the root.
     * @param folder condition for visited folders.
     */
    void travel(Consumer<? super Folder<T>> folder);
}
