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

/**
 * Each tree folder filesystem has a node. In our case, folder is a node that has children and does not have a parent.
 *
 * It's necessary to separate such entities as Folder and Node: in filesystems folder is a node, but in different cases
 * folder may not be a node.
 * @param <T> Child type.
 */
public interface Node<T> {

    /**
     * @return List of child node.
     */
    List<T> children();
}
