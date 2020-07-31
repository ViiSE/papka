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

import java.util.List;

/**
 * Full folder path from folder names.
 * @see ru.viise.papka.entity.Name
 */
public class NameFoldersPath implements Name {

    /**
     * Full name.
     */
    private String fullName = "";

    /**
     * Short name.
     */
    private String shortName = "";

    /**
     * {@link Separator}.
     */
    private final Separator separator;

    /**
     * List of folders name;
     */
    private final List<String> foldersName;

    /**
     * Ctor.
     * @param foldersName List of folders name.
     * @param separator {@link Separator}.
     */
    public NameFoldersPath(List<String> foldersName, Separator separator) {
        this.separator = separator;
        this.foldersName = foldersName;
    }

    /**
     * Ctor.
     * @param foldersName List of folders name.
     */
    public NameFoldersPath(List<String> foldersName) {
        this(foldersName, new SeparatorUnix());
    }

    @Override
    public String shortName() {
        if(shortName.isEmpty())
            shortName = _shortName();
        return shortName;
    }

    @Override
    public String fullName() {
        if(fullName.isEmpty())
            fullName = _fullName();
        return fullName;
    }

    @Override
    public boolean equals(Object name) {
        if(!(name instanceof Name))
            return false;

        if(((Name) name).fullName().equals(fullName()))
                return true;

        return super.equals(name);
    }

    private String _fullName() {
        String fullName = String.join(separator.pure(), foldersName) + separator.pure();
        return fullName.replace(separator.pure() + separator.pure(), separator.pure());
    }

    private String _shortName() {
        return foldersName.get(foldersName.size() - 1);
    }
}
