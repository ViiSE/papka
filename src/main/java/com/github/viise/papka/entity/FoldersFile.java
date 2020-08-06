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
 * FoldersFile is necessary from generating tree folder. It allows get short filename and the short names of folders in
 * which file is located.
 * @param <T> Type of folder.
 * @param <V> Type of file.
 */
public interface FoldersFile<T, V> {

    /**
     * @param file File to create a list of folders.
     * @return list of file folders.
     */
    List<T> folders(V file);
}
